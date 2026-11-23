package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.ReclamoRequest;
import co.edu.unipiloto.plataforma.dto.ReclamoRespuestaRequest;
import co.edu.unipiloto.plataforma.dto.ReclamoResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.ReclamoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Reclamos por evaluacion.
 *  - El estudiante reclama la nota de una entrega suya ya calificada (hasta 10 dias despues de la calificacion).
 *  - El docente del curso responde: ACEPTADO (con la nota nueva) o RECHAZADO (con la justificacion).
 *  - Cada paso genera una notificacion a la otra parte.
 */
@Service
public class ReclamoServicio {

    public static final int DIAS_PARA_RECLAMAR = 10;

    private final ReclamoRepositorio reclamos;
    private final NotificacionServicio notificaciones;
    private final Buscador buscar;

    public ReclamoServicio(ReclamoRepositorio reclamos, NotificacionServicio notificaciones, Buscador buscar) {
        this.reclamos = reclamos;
        this.notificaciones = notificaciones;
        this.buscar = buscar;
    }

    @Transactional
    public ReclamoResponse presentar(ReclamoRequest req) {
        Entrega entrega = buscar.entrega(req.getEntregaId());
        Usuario est = buscar.usuario(req.getEstudianteId());
        if (!entrega.getEstudiante().getId().equals(est.getId())) {
            throw new ProhibidoException("Solo el estudiante dueno de la entrega puede reclamar su calificacion");
        }
        if (!entrega.estaCalificada()) {
            throw new ReglaNegocioException("La entrega aun no ha sido calificada: no hay nada que reclamar");
        }
        if (entrega.getFechaCalificacion() != null
                && LocalDateTime.now().isAfter(entrega.getFechaCalificacion().plusDays(DIAS_PARA_RECLAMAR))) {
            throw new ReglaNegocioException("El plazo para reclamar (" + DIAS_PARA_RECLAMAR + " dias desde la calificacion) ya vencio");
        }
        for (Reclamo previo : reclamos.findByEntrega_Id(entrega.getId())) {
            if (previo.getEstado() == EstadoReclamo.ABIERTO) {
                throw new ConflictoException("Ya hay un reclamo abierto para esta entrega");
            }
        }
        Curso curso = entrega.getActividad().getUnidad().getCurso();
        Reclamo r = new Reclamo();
        r.setEntrega(entrega);
        r.setEstudiante(est);
        r.setMotivo(req.getMotivo().trim());
        r.setEstado(EstadoReclamo.ABIERTO);
        r.setNotaAnterior(entrega.getCalificacion());
        r.setFechaCreacion(LocalDateTime.now());
        r = reclamos.save(r);
        notificaciones.crear(curso.getDocente(), est, TipoNotificacion.RECLAMO,
                "Reclamo por " + entrega.getActividad().getTitulo(),
                est.getNombre() + " reclama su nota de " + entrega.getCalificacion() + ". Motivo: " + r.getMotivo(),
                curso.getId());
        return Conversor.aReclamo(r);
    }

    @Transactional
    public ReclamoResponse responder(Long id, ReclamoRespuestaRequest req) {
        Reclamo r = reclamo(id);
        Entrega entrega = r.getEntrega();
        Curso curso = entrega.getActividad().getUnidad().getCurso();
        Usuario docente = buscar.usuario(req.getDocenteId());
        if (!Roles.DOCENTE.equalsIgnoreCase(docente.getRol().getNombre())
                || !curso.getDocente().getId().equals(docente.getId())) {
            throw new ProhibidoException("Solo el docente de este curso puede responder el reclamo");
        }
        if (r.getEstado() != EstadoReclamo.ABIERTO) {
            throw new ConflictoException("Este reclamo ya fue respondido (" + r.getEstado() + ")");
        }
        if (req.getDecision() != EstadoReclamo.ACEPTADO && req.getDecision() != EstadoReclamo.RECHAZADO) {
            throw new ReglaNegocioException("La decision debe ser ACEPTADO o RECHAZADO");
        }
        if (req.getDecision() == EstadoReclamo.ACEPTADO) {
            if (req.getNotaNueva() == null) {
                throw new ReglaNegocioException("Para aceptar el reclamo indique la notaNueva");
            }
            r.setNotaNueva(Numeros.redondear(req.getNotaNueva()));
            entrega.setCalificacion(r.getNotaNueva());
            entrega.setFechaCalificacion(LocalDateTime.now());
        }
        r.setEstado(req.getDecision());
        r.setRespuesta(req.getRespuesta().trim());
        r.setDocente(docente);
        r.setFechaRespuesta(LocalDateTime.now());
        r = reclamos.save(r);

        String resultado = r.getEstado() == EstadoReclamo.ACEPTADO
                ? "Reclamo aceptado. Su nota paso de " + r.getNotaAnterior() + " a " + r.getNotaNueva() + ". "
                : "Reclamo rechazado. La nota se mantiene en " + r.getNotaAnterior() + ". ";
        notificaciones.crear(r.getEstudiante(), docente, TipoNotificacion.RESPUESTA_RECLAMO,
                "Respuesta a su reclamo: " + entrega.getActividad().getTitulo(), resultado + r.getRespuesta(), curso.getId());
        return Conversor.aReclamo(r);
    }

    @Transactional(readOnly = true)
    public List<ReclamoResponse> listar(Long estudianteId, Long cursoId, Long docenteId, EstadoReclamo estado) {
        List<Reclamo> base;
        if (estudianteId != null) {
            base = reclamos.findByEstudiante_Id(estudianteId);
        } else if (docenteId != null) {
            base = reclamos.findByEntrega_Actividad_Unidad_Curso_Docente_Id(docenteId);
        } else if (cursoId != null) {
            base = reclamos.findByEntrega_Actividad_Unidad_Curso_Id(cursoId);
        } else {
            base = reclamos.findAll();
        }
        List<ReclamoResponse> salida = new ArrayList<>();
        for (Reclamo r : base) {
            if (estado == null || r.getEstado() == estado) {
                salida.add(Conversor.aReclamo(r));
            }
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public ReclamoResponse obtener(Long id) {
        return Conversor.aReclamo(reclamo(id));
    }

    private Reclamo reclamo(Long id) {
        return reclamos.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("El reclamo", id));
    }
}
