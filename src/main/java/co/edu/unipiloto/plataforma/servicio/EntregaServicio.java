package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.CalificacionRequest;
import co.edu.unipiloto.plataforma.dto.EntregaActualizarRequest;
import co.edu.unipiloto.plataforma.dto.EntregaRequest;
import co.edu.unipiloto.plataforma.dto.EntregaResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.EntregaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Procesos formativos y de evaluacion:
 *  - el estudiante entrega una actividad (dentro de las fechas y estando matriculado),
 *  - el docente del curso califica y deja retroalimentacion.
 */
@Service
public class EntregaServicio {

    private final EntregaRepositorio entregas;
    private final MatriculaRepositorio matriculas;
    private final Buscador buscar;

    public EntregaServicio(EntregaRepositorio entregas, MatriculaRepositorio matriculas, Buscador buscar) {
        this.entregas = entregas;
        this.matriculas = matriculas;
        this.buscar = buscar;
    }

    @Transactional(readOnly = true)
    public List<EntregaResponse> listar(Long actividadId, Long estudianteId, Long cursoId) {
        List<Entrega> base;
        if (actividadId != null && estudianteId != null) {
            base = new ArrayList<>();
            entregas.findByActividad_IdAndEstudiante_Id(actividadId, estudianteId).ifPresent(base::add);
        } else if (actividadId != null) {
            base = entregas.findByActividad_Id(actividadId);
        } else if (cursoId != null && estudianteId != null) {
            base = entregas.findByActividad_Unidad_Curso_IdAndEstudiante_Id(cursoId, estudianteId);
        } else if (cursoId != null) {
            base = entregas.findByActividad_Unidad_Curso_Id(cursoId);
        } else if (estudianteId != null) {
            base = entregas.findByEstudiante_Id(estudianteId);
        } else {
            base = entregas.findAll();
        }
        List<EntregaResponse> salida = new ArrayList<>();
        for (Entrega e : base) {
            salida.add(Conversor.aEntrega(e));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public EntregaResponse obtener(Long id) {
        return Conversor.aEntrega(buscar.entrega(id));
    }

    @Transactional
    public EntregaResponse entregar(EntregaRequest req) {
        Actividad act = buscar.actividad(req.getActividadId());
        Usuario est = buscar.usuario(req.getEstudianteId());
        Long cursoId = act.getUnidad().getCurso().getId();

        if (!Roles.ESTUDIANTE.equalsIgnoreCase(est.getRol().getNombre())) {
            throw new ProhibidoException("Solo los estudiantes pueden entregar actividades");
        }
        if (!matriculas.existsByEstudiante_IdAndCurso_Id(est.getId(), cursoId)) {
            throw new ProhibidoException("El estudiante no esta matriculado en el curso de esta actividad");
        }
        verificarVentana(act);
        if (entregas.findByActividad_IdAndEstudiante_Id(act.getId(), est.getId()).isPresent()) {
            throw new ConflictoException("El estudiante ya entrego esta actividad. Use PUT para modificarla");
        }
        verificarContenido(act, req.getTextoRespuesta(), req.getNombreArchivo());

        Entrega e = new Entrega();
        e.setActividad(act);
        e.setEstudiante(est);
        e.setTextoRespuesta(req.getTextoRespuesta());
        e.setNombreArchivo(req.getNombreArchivo());
        e.setFechaEntrega(LocalDateTime.now());
        return Conversor.aEntrega(entregas.save(e));
    }

    @Transactional
    public EntregaResponse modificar(Long id, EntregaActualizarRequest req) {
        Entrega e = buscar.entrega(id);
        if (e.estaCalificada()) {
            throw new ConflictoException("La entrega ya fue calificada y no se puede modificar");
        }
        verificarVentana(e.getActividad());
        verificarContenido(e.getActividad(), req.getTextoRespuesta(), req.getNombreArchivo());
        e.setTextoRespuesta(req.getTextoRespuesta());
        e.setNombreArchivo(req.getNombreArchivo());
        e.setFechaEntrega(LocalDateTime.now());
        return Conversor.aEntrega(entregas.save(e));
    }

    /** Califica y retroalimenta. Solo el docente dueno del curso puede hacerlo. */
    @Transactional
    public EntregaResponse calificar(Long id, CalificacionRequest req) {
        Entrega e = buscar.entrega(id);
        Usuario docente = buscar.usuario(req.getDocenteId());
        Curso curso = e.getActividad().getUnidad().getCurso();
        if (!Roles.DOCENTE.equalsIgnoreCase(docente.getRol().getNombre())
                || !curso.getDocente().getId().equals(docente.getId())) {
            throw new ProhibidoException("Solo el docente de este curso puede calificar sus entregas");
        }
        e.setCalificacion(Numeros.redondear(req.getNota()));
        e.setRetroalimentacion(req.getRetroalimentacion());
        e.setFechaCalificacion(LocalDateTime.now());
        return Conversor.aEntrega(entregas.save(e));
    }

    @Transactional
    public void eliminar(Long id) {
        Entrega e = buscar.entrega(id);
        if (e.estaCalificada()) {
            throw new ConflictoException("No se puede eliminar una entrega ya calificada");
        }
        entregas.delete(e);
    }

    private void verificarVentana(Actividad act) {
        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isBefore(act.getFechaInicio())) {
            throw new ReglaNegocioException("La actividad todavia no esta habilitada (abre el " + act.getFechaInicio() + ")");
        }
        if (ahora.isAfter(act.getFechaLimite())) {
            throw new ReglaNegocioException("El plazo de entrega vencio el " + act.getFechaLimite());
        }
    }

    private void verificarContenido(Actividad act, String texto, String archivo) {
        boolean sinTexto = texto == null || texto.trim().isEmpty();
        boolean sinArchivo = archivo == null || archivo.trim().isEmpty();
        if (act.getTipoEntrega() == TipoEntrega.ARCHIVO && sinArchivo) {
            throw new ReglaNegocioException("Esta actividad pide un archivo: envie nombreArchivo");
        }
        if ((act.getTipoEntrega() == TipoEntrega.TEXTO || act.getTipoEntrega() == TipoEntrega.PREGUNTAS) && sinTexto) {
            throw new ReglaNegocioException("Esta actividad pide una respuesta: envie textoRespuesta");
        }
    }
}
