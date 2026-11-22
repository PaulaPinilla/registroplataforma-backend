package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.MatriculaRequest;
import co.edu.unipiloto.plataforma.dto.MatriculaResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.CursoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.EntregaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.EvaluacionCursoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Proceso formativo: matricula de estudiantes en cursos.
 * Reglas: solo usuarios con rol ESTUDIANTE, curso vigente, clave correcta, oferta abierta y con cupos.
 */
@Service
public class MatriculaServicio {

    private final MatriculaRepositorio matriculas;
    private final CursoRepositorio cursos;
    private final EntregaRepositorio entregas;
    private final EvaluacionCursoRepositorio evaluaciones;
    private final Buscador buscar;

    public MatriculaServicio(MatriculaRepositorio matriculas, CursoRepositorio cursos, EntregaRepositorio entregas,
                             EvaluacionCursoRepositorio evaluaciones, Buscador buscar) {
        this.matriculas = matriculas;
        this.cursos = cursos;
        this.entregas = entregas;
        this.evaluaciones = evaluaciones;
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
        return Conversor.aMatricula(matriculas.save(m));
    }

    /** Cancela la matricula y borra las entregas y la evaluacion que el estudiante tenia en ese curso. */
    @Transactional
    public void cancelar(Long id) {
        Matricula m = matriculas.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.de("La matricula", id));
        Long cursoId = m.getCurso().getId();
        Long estId = m.getEstudiante().getId();
        entregas.deleteAll(entregas.findByActividad_Unidad_Curso_IdAndEstudiante_Id(cursoId, estId));
        evaluaciones.findByCurso_IdAndEstudiante_Id(cursoId, estId).ifPresent(evaluaciones::delete);
        matriculas.delete(m);
    }
}
