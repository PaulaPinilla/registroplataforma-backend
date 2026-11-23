package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.ForoRequest;
import co.edu.unipiloto.plataforma.dto.ForoResponse;
import co.edu.unipiloto.plataforma.dto.MensajeRequest;
import co.edu.unipiloto.plataforma.dto.MensajeResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.ForoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MensajeForoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Foros y chats de un curso. El docente los abre y los cierra; participan el docente y los estudiantes matriculados.
 * El chat es sincronico por sondeo: el cliente consulta los mensajes nuevos con GET .../mensajes?despuesDeId=ultimoId.
 */
@Service
public class ForoServicio {

    private final ForoRepositorio foros;
    private final MensajeForoRepositorio mensajes;
    private final Permisos permisos;
    private final NotificacionServicio notificaciones;
    private final Buscador buscar;

    public ForoServicio(ForoRepositorio foros, MensajeForoRepositorio mensajes, Permisos permisos,
                        NotificacionServicio notificaciones, Buscador buscar) {
        this.foros = foros;
        this.mensajes = mensajes;
        this.permisos = permisos;
        this.notificaciones = notificaciones;
        this.buscar = buscar;
    }

    @Transactional
    public ForoResponse crear(ForoRequest req) {
        Curso curso = permisos.exigirDocenteDelCurso(req.getCursoId(), req.getDocenteId(), "abrir foros o chats");
        Foro f = new Foro();
        f.setCurso(curso);
        f.setCreador(curso.getDocente());
        f.setTitulo(req.getTitulo().trim());
        f.setDescripcion(req.getDescripcion());
        f.setTipo(req.getTipo());
        f.setAbierto(true);
        f.setFechaCreacion(LocalDateTime.now());
        f = foros.save(f);
        notificaciones.notificarMatriculados(curso, curso.getDocente(), TipoNotificacion.FORO_NUEVO,
                "Nuevo " + (f.getTipo() == TipoForo.CHAT ? "chat" : "foro") + " en " + curso.getTitulo(),
                "El docente abrio: " + f.getTitulo());
        return Conversor.aForo(f, 0);
    }

    @Transactional(readOnly = true)
    public List<ForoResponse> listarPorCurso(Long cursoId) {
        buscar.curso(cursoId);
        List<ForoResponse> salida = new ArrayList<>();
        for (Foro f : foros.findByCurso_Id(cursoId)) {
            salida.add(Conversor.aForo(f, mensajes.countByForo_Id(f.getId())));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public ForoResponse obtener(Long id) {
        Foro f = foro(id);
        return Conversor.aForo(f, mensajes.countByForo_Id(id));
    }

    /** El docente abre o cierra el foro. Un foro cerrado no admite mensajes nuevos. */
    @Transactional
    public ForoResponse cambiarEstado(Long id, Long docenteId, boolean abierto) {
        Foro f = foro(id);
        permisos.exigirDocenteDelCurso(f.getCurso().getId(), docenteId, "abrir o cerrar foros");
        f.setAbierto(abierto);
        return Conversor.aForo(foros.save(f), mensajes.countByForo_Id(id));
    }

    @Transactional
    public void eliminar(Long id, Long docenteId) {
        Foro f = foro(id);
        permisos.exigirDocenteDelCurso(f.getCurso().getId(), docenteId, "eliminar foros");
        mensajes.deleteByForo_Id(id);
        foros.delete(f);
    }

    @Transactional
    public MensajeResponse publicar(Long foroId, MensajeRequest req) {
        Foro f = foro(foroId);
        Usuario autor = buscar.usuario(req.getAutorId());
        if (!permisos.esMiembroDelCurso(f.getCurso(), autor)) {
            throw new ProhibidoException("Solo el docente y los estudiantes matriculados pueden participar en este foro");
        }
        if (!f.estaAbierto()) {
            throw new ConflictoException("El foro esta cerrado y no admite mensajes nuevos");
        }
        if (req.getRespuestaAId() != null) {
            MensajeForo padre = mensajes.findById(req.getRespuestaAId())
                    .orElseThrow(() -> RecursoNoEncontradoException.de("El mensaje al que responde", req.getRespuestaAId()));
            if (!padre.getForo().getId().equals(foroId)) {
                throw new ReglaNegocioException("El mensaje al que responde pertenece a otro foro");
            }
        }
        MensajeForo m = new MensajeForo();
        m.setForo(f);
        m.setAutor(autor);
        m.setContenido(req.getContenido().trim());
        m.setRespuestaAId(req.getRespuestaAId());
        m.setFecha(LocalDateTime.now());
        return Conversor.aMensaje(mensajes.save(m));
    }

    /** Lista los mensajes. Con despuesDeId devuelve solo los nuevos (para el chat). */
    @Transactional(readOnly = true)
    public List<MensajeResponse> listarMensajes(Long foroId, Long usuarioId, Long despuesDeId) {
        Foro f = foro(foroId);
        Usuario u = buscar.usuario(usuarioId);
        if (!permisos.esMiembroDelCurso(f.getCurso(), u)) {
            throw new ProhibidoException("Solo el docente y los estudiantes matriculados pueden ver este foro");
        }
        List<MensajeForo> base = despuesDeId == null
                ? mensajes.findByForo_IdOrderByIdAsc(foroId)
                : mensajes.findByForo_IdAndIdGreaterThanOrderByIdAsc(foroId, despuesDeId);
        List<MensajeResponse> salida = new ArrayList<>();
        for (MensajeForo m : base) {
            salida.add(Conversor.aMensaje(m));
        }
        return salida;
    }

    private Foro foro(Long id) {
        return foros.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("El foro", id));
    }
}
