package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.MatriculaRequest;
import co.edu.unipiloto.plataforma.dto.MatriculaResponse;
import co.edu.unipiloto.plataforma.dto.RetiroRequest;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Proceso formativo: inscripcion y retiro de estudiantes en cursos.
 * Inscripcion: solo ESTUDIANTE activo, curso publicado y vigente, clave correcta, cupos del curso y de la oferta.
 * Retiro: no se permite si el curso ya finalizo o si ya se emitio el certificado.
 */
@Service
public class MatriculaServicio {

    private final MatriculaRepositorio matriculas;
    private final CursoRepositorio cursos;
    private final EntregaRepositorio entregas;
    private final EvaluacionCursoRepositorio evaluaciones;
    private final ReclamoRepositorio reclamos;
    private final CertificadoRepositorio certificados;
    private final ArchivoServicio archivos;
    private final NotificacionServicio notificaciones;
    private final Buscador buscar;

    public MatriculaServicio(MatriculaRepositorio matriculas, CursoRepositorio cursos, EntregaRepositorio entregas,
                             EvaluacionCursoRepositorio evaluaciones, ReclamoRepositorio reclamos,
                             CertificadoRepositorio certificados, ArchivoServicio archivos,
                             NotificacionServicio notificaciones, Buscador buscar) {
        this.matriculas = matriculas;
        this.cursos = cursos;
        this.entregas = entregas;
        this.evaluaciones = evaluaciones;
        this.reclamos = reclamos;
        this.certificados = certificados;
        this.archivos = archivos;
        this.notificaciones = notificaciones;
        this.buscar = buscar;
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponse> listar(Long estudianteId, Long cursoId) {
        List<Matricula> base;
        if (estudianteId != null) {
            base = matriculas.findByEstudiante_Id(estudianteId);
        } else if (cursoId != null) {
            base = matriculas.findByCurso_Id(cursoId);
        } else {
            base = matriculas.findAll();
        }
        List<MatriculaResponse> salida = new ArrayList<>();
        for (Matricula m : base) {
            salida.add(Conversor.aMatricula(m));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public MatriculaResponse obtener(Long id) {
        return Conversor.aMatricula(matriculas.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.de("La matricula", id)));
    }

    @Transactional
    public MatriculaResponse matricular(MatriculaRequest req) {
        Usuario est = buscar.usuario(req.getEstudianteId());
        Curso curso = buscar.curso(req.getCursoId());

        if (!Roles.ESTUDIANTE.equalsIgnoreCase(est.getRol().getNombre())) {
            throw new ProhibidoException("Solo los usuarios con rol ESTUDIANTE pueden matricularse");
        }
        if (!est.isActivo()) {
            throw new ReglaNegocioException("El estudiante esta desactivado");
        }
        if (!curso.estaPublicado()) {
            throw new ReglaNegocioException("El curso aun no esta publicado por el coordinador");
        }
        if (curso.yaTermino()) {
            throw new ReglaNegocioException("El curso ya finalizo, no admite matriculas");
        }
        if (matriculas.existsByEstudiante_IdAndCurso_Id(est.getId(), curso.getId())) {
            throw new ConflictoException("El estudiante ya esta matriculado en este curso");
        }
        if (curso.exigeClave()) {
            String clave = req.getClave();
            if (clave == null || !clave.equals(curso.getClaveMatricula())) {
                throw new ProhibidoException("La clave de matricula es incorrecta");
            }
        }
        if (curso.getCupos() != null && matriculas.findByCurso_Id(curso.getId()).size() >= curso.getCupos()) {
            throw new ConflictoException("El curso no tiene cupos disponibles");
        }
        OfertaEducativa oferta = curso.getOferta();
        if (oferta != null) {
            if (oferta.getEstado() == EstadoOferta.CERRADA) {
                throw new ReglaNegocioException("La oferta del curso esta CERRADA");
            }
            long usados = 0;
            for (Curso c : cursos.findByOferta_Id(oferta.getId())) {
                usados += matriculas.findByCurso_Id(c.getId()).size();
            }
            if (oferta.getCupos() != null && usados >= oferta.getCupos()) {
                throw new ConflictoException("La oferta no tiene cupos disponibles");
            }
        }
        Matricula m = new Matricula();
        m.setEstudiante(est);
        m.setCurso(curso);
        m.setFechaMatricula(LocalDateTime.now());
        m = matriculas.save(m);
        notificaciones.crear(curso.getDocente(), est, TipoNotificacion.MENSAJE_GENERAL,
                "Nueva matricula en " + curso.getTitulo(),
                est.getNombre() + " se matriculo en su curso '" + curso.getTitulo() + "'.", curso.getId());
        return Conversor.aMatricula(m);
    }

    /** Retiro por id de matricula. */
    @Transactional
    public void cancelar(Long id) {
        Matricula m = matriculas.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.de("La matricula", id));
        retirar(m);
    }

    /** Retiro indicando estudiante y curso. */
    @Transactional
    public void retirar(RetiroRequest req) {
        Matricula m = matriculas.findByEstudiante_IdAndCurso_Id(req.getEstudianteId(), req.getCursoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El estudiante " + req.getEstudianteId()
                        + " no esta matriculado en el curso " + req.getCursoId()));
        retirar(m);
    }

    /** Elimina la matricula y las entregas, reclamos y evaluacion que el estudiante tenia en ese curso. */
    private void retirar(Matricula m) {
        Curso curso = m.getCurso();
        Usuario est = m.getEstudiante();
        if (curso.yaTermino()) {
            throw new ReglaNegocioException("El curso ya finalizo: no es posible retirarse");
        }
        if (certificados.findByEstudiante_IdAndCurso_Id(est.getId(), curso.getId()).isPresent()) {
            throw new ConflictoException("No es posible retirarse: ya se emitio el certificado de este curso");
        }
        List<Entrega> propias = entregas.findByActividad_Unidad_Curso_IdAndEstudiante_Id(curso.getId(), est.getId());
        for (Entrega e : propias) {
            reclamos.deleteByEntrega_Id(e.getId());
            archivos.eliminar(e.getRutaArchivo());
        }
        entregas.deleteAll(propias);
        evaluaciones.findByCurso_IdAndEstudiante_Id(curso.getId(), est.getId()).ifPresent(evaluaciones::delete);
        matriculas.delete(m);
        notificaciones.crear(curso.getDocente(), est, TipoNotificacion.RETIRO,
                "Retiro de " + curso.getTitulo(),
                est.getNombre() + " se retiro del curso '" + curso.getTitulo() + "'.", curso.getId());
    }
}
