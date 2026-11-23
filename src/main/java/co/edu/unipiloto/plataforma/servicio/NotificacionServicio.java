package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.EnvioNotificacionRequest;
import co.edu.unipiloto.plataforma.dto.EnvioNotificacionResponse;
import co.edu.unipiloto.plataforma.dto.NotificacionResponse;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.NotificacionRepositorio;
import co.edu.unipiloto.plataforma.repositorio.RolRepositorio;
import co.edu.unipiloto.plataforma.repositorio.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Notificaciones: las genera el sistema ante eventos (asignacion de curso, entrega, calificacion...)
 * y las envian manualmente el coordinador (a cualquiera) o el docente (a los estudiantes de su curso).
 */
@Service
public class NotificacionServicio {

    private final NotificacionRepositorio notificaciones;
    private final UsuarioRepositorio usuarios;
    private final RolRepositorio roles;
    private final MatriculaRepositorio matriculas;
    private final Buscador buscar;

    public NotificacionServicio(NotificacionRepositorio notificaciones, UsuarioRepositorio usuarios,
                                RolRepositorio roles, MatriculaRepositorio matriculas, Buscador buscar) {
        this.notificaciones = notificaciones;
        this.usuarios = usuarios;
        this.roles = roles;
        this.matriculas = matriculas;
        this.buscar = buscar;
    }

    /** Crea una notificacion (usado por los demas servicios dentro de su transaccion). */
    public Notificacion crear(Usuario destinatario, Usuario remitente, TipoNotificacion tipo, String titulo,
                              String mensaje, Long cursoId) {
        Notificacion n = new Notificacion();
        n.setDestinatario(destinatario);
        n.setRemitente(remitente);
        n.setTipo(tipo);
        n.setTitulo(recortar(titulo, 150));
        n.setMensaje(recortar(mensaje, 1000));
        n.setCursoId(cursoId);
        n.setLeida(false);
        n.setFecha(LocalDateTime.now());
        return notificaciones.save(n);
    }

    /** Notifica a todos los estudiantes matriculados en un curso. */
    public int notificarMatriculados(Curso curso, Usuario remitente, TipoNotificacion tipo, String titulo, String mensaje) {
        int total = 0;
        for (Matricula m : matriculas.findByCurso_Id(curso.getId())) {
            crear(m.getEstudiante(), remitente, tipo, titulo, mensaje, curso.getId());
            total++;
        }
        return total;
    }

    /** Notifica a todos los usuarios activos de un rol. */
    public int notificarRol(String nombreRol, Usuario remitente, TipoNotificacion tipo, String titulo, String mensaje,
                            Long cursoId) {
        int total = 0;
        for (Usuario u : usuariosDeRol(nombreRol)) {
            if (u.isActivo()) {
                crear(u, remitente, tipo, titulo, mensaje, cursoId);
                total++;
            }
        }
        return total;
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> listar(Long usuarioId, boolean soloNoLeidas) {
        buscar.usuario(usuarioId);
        List<Notificacion> base = soloNoLeidas
                ? notificaciones.findByDestinatario_IdAndLeidaOrderByIdDesc(usuarioId, Boolean.FALSE)
                : notificaciones.findByDestinatario_IdOrderByIdDesc(usuarioId);
        List<NotificacionResponse> salida = new ArrayList<>();
        for (Notificacion n : base) {
            salida.add(Conversor.aNotificacion(n));
        }
        return salida;
    }

    @Transactional
    public NotificacionResponse marcarLeida(Long id) {
        Notificacion n = notificaciones.findById(id)
                .orElseThrow(() -> co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException.de("La notificacion", id));
        n.setLeida(true);
        return Conversor.aNotificacion(notificaciones.save(n));
    }

    @Transactional
    public int marcarTodasLeidas(Long usuarioId) {
        buscar.usuario(usuarioId);
        int total = 0;
        for (Notificacion n : notificaciones.findByDestinatario_IdAndLeidaOrderByIdDesc(usuarioId, Boolean.FALSE)) {
            n.setLeida(true);
            notificaciones.save(n);
            total++;
        }
        return total;
    }

    @Transactional
    public EnvioNotificacionResponse enviar(EnvioNotificacionRequest req) {
        Usuario remitente = buscar.usuario(req.getRemitenteId());
        String rol = remitente.getRol().getNombre();
        boolean esCoordinador = Roles.COORDINADOR.equalsIgnoreCase(rol);
        boolean esDocente = Roles.DOCENTE.equalsIgnoreCase(rol);
        if (!esCoordinador && !esDocente) {
            throw new ProhibidoException("Solo el coordinador o un docente pueden enviar notificaciones");
        }
        if (esDocente && req.getDestino() != DestinoNotificacion.CURSO) {
            throw new ProhibidoException("Un docente solo puede notificar a los estudiantes de su curso (destino CURSO)");
        }

        // se arma el conjunto de destinatarios sin repetir
        Map<Long, Usuario> destinatarios = new LinkedHashMap<>();
        switch (req.getDestino()) {
            case USUARIO:
                if (req.getUsuarioId() == null) {
                    throw new ReglaNegocioException("Para el destino USUARIO debe enviar usuarioId");
                }
                Usuario u = buscar.usuario(req.getUsuarioId());
                destinatarios.put(u.getId(), u);
                break;
            case DOCENTES:
                for (Usuario d : usuariosDeRol(Roles.DOCENTE)) {
                    destinatarios.put(d.getId(), d);
                }
                break;
            case ESTUDIANTES:
                for (Usuario e : usuariosDeRol(Roles.ESTUDIANTE)) {
                    destinatarios.put(e.getId(), e);
                }
                break;
            case CURSO:
                if (req.getCursoId() == null) {
                    throw new ReglaNegocioException("Para el destino CURSO debe enviar cursoId");
                }
                Curso curso = buscar.curso(req.getCursoId());
                if (esDocente && !curso.getDocente().getId().equals(remitente.getId())) {
                    throw new ProhibidoException("Solo el docente de este curso puede notificar a sus estudiantes");
                }
                for (Matricula m : matriculas.findByCurso_Id(curso.getId())) {
                    destinatarios.put(m.getEstudiante().getId(), m.getEstudiante());
                }
                break;
            default:
                for (Usuario t : usuarios.findAll()) {
                    destinatarios.put(t.getId(), t);
                }
                break;
        }
        destinatarios.remove(remitente.getId());

        int enviados = 0;
        for (Usuario d : destinatarios.values()) {
            if (d.isActivo()) {
                crear(d, remitente, TipoNotificacion.MENSAJE_GENERAL, req.getTitulo(), req.getMensaje(), req.getCursoId());
                enviados++;
            }
        }
        EnvioNotificacionResponse r = new EnvioNotificacionResponse();
        r.setDestinatarios(enviados);
        r.setMensaje("Notificacion enviada a " + enviados + " usuario(s)");
        return r;
    }

    private List<Usuario> usuariosDeRol(String nombreRol) {
        List<Usuario> salida = new ArrayList<>();
        Rol rol = roles.findByNombreIgnoreCase(nombreRol).orElse(null);
        if (rol != null) {
            salida.addAll(usuarios.findByRol_Id(rol.getId()));
        }
        return salida;
    }

    private static String recortar(String texto, int max) {
        if (texto == null) {
            return null;
        }
        return texto.length() <= max ? texto : texto.substring(0, max);
    }
}
