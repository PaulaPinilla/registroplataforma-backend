package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.*;
import co.edu.unipiloto.plataforma.modelo.*;

/**
 * Convierte entidades en DTO de respuesta.
 * Asi la API nunca expone la entidad completa (por ejemplo, la contrasena cifrada).
 */
public final class Conversor {

    private Conversor() {
    }

    public static RolResponse aRol(Rol r) {
        RolResponse d = new RolResponse();
        d.setId(r.getId());
        d.setNombre(r.getNombre());
        d.setDescripcion(r.getDescripcion());
        return d;
    }

    public static UsuarioResponse aUsuario(Usuario u) {
        UsuarioResponse d = new UsuarioResponse();
        d.setId(u.getId());
        d.setNombre(u.getNombre());
        d.setCorreo(u.getCorreo());
        d.setRolId(u.getRol().getId());
        d.setRol(u.getRol().getNombre());
        d.setActivo(u.isActivo());
        d.setFechaRegistro(u.getFechaRegistro());
        return d;
    }

    public static OfertaResponse aOferta(OfertaEducativa o, long matriculasActuales) {
        OfertaResponse d = new OfertaResponse();
        d.setId(o.getId());
        d.setNombre(o.getNombre());
        d.setDescripcion(o.getDescripcion());
        d.setModalidad(o.getModalidad());
        d.setFechaInicio(o.getFechaInicio());
        d.setFechaFin(o.getFechaFin());
        d.setCupos(o.getCupos());
        d.setEstado(o.getEstado());
        d.setMatriculasActuales(matriculasActuales);
        long cupos = o.getCupos() == null ? 0 : o.getCupos();
        d.setCuposDisponibles(Math.max(0, cupos - matriculasActuales));
        return d;
    }

    public static CursoResponse aCurso(Curso c) {
        CursoResponse d = new CursoResponse();
        d.setId(c.getId());
        d.setTitulo(c.getTitulo());
        d.setDescripcion(c.getDescripcion());
        d.setSemestre(c.getSemestre());
        d.setFechaFinalizacion(c.getFechaFinalizacion());
        d.setPesoCorte1(c.getPesoCorte1());
        d.setPesoCorte2(c.getPesoCorte2());
        d.setPesoCorte3(c.getPesoCorte3());
        d.setExigeClave(c.exigeClave());
        d.setDocenteId(c.getDocente().getId());
        d.setDocenteNombre(c.getDocente().getNombre());
        if (c.getOferta() != null) {
            d.setOfertaId(c.getOferta().getId());
            d.setOfertaNombre(c.getOferta().getNombre());
        }
        d.setFinalizado(c.yaTermino());
        d.setObjetivos(c.getObjetivos());
        d.setResultadosAprendizaje(c.getResultadosAprendizaje());
        d.setCupos(c.getCupos());
        d.setFechaInicio(c.getFechaInicio());
        d.setPublicado(c.estaPublicado());
        return d;
    }

    public static UnidadResponse aUnidad(Unidad u) {
        UnidadResponse d = new UnidadResponse();
        d.setId(u.getId());
        d.setTitulo(u.getTitulo());
        d.setOrden(u.getOrden());
        d.setCursoId(u.getCurso().getId());
        d.setCursoTitulo(u.getCurso().getTitulo());
        return d;
    }

    public static ActividadResponse aActividad(Actividad a) {
        ActividadResponse d = new ActividadResponse();
        d.setId(a.getId());
        d.setTitulo(a.getTitulo());
        d.setInstrucciones(a.getInstrucciones());
        d.setFechaInicio(a.getFechaInicio());
        d.setFechaLimite(a.getFechaLimite());
        d.setTipoEntrega(a.getTipoEntrega());
        d.setCategoria(a.getCategoria());
        d.setCorte(a.getCorte());
        d.setPorcentaje(a.getPorcentaje());
        d.setFechaCreacion(a.getFechaCreacion());
        d.setUnidadId(a.getUnidad().getId());
        d.setUnidadTitulo(a.getUnidad().getTitulo());
        d.setCursoId(a.getUnidad().getCurso().getId());
        return d;
    }

    public static MatriculaResponse aMatricula(Matricula m) {
        MatriculaResponse d = new MatriculaResponse();
        d.setId(m.getId());
        d.setEstudianteId(m.getEstudiante().getId());
        d.setEstudianteNombre(m.getEstudiante().getNombre());
        d.setCursoId(m.getCurso().getId());
        d.setCursoTitulo(m.getCurso().getTitulo());
        d.setFechaMatricula(m.getFechaMatricula());
        return d;
    }

    public static EntregaResponse aEntrega(Entrega e) {
        EntregaResponse d = new EntregaResponse();
        d.setId(e.getId());
        d.setActividadId(e.getActividad().getId());
        d.setActividadTitulo(e.getActividad().getTitulo());
        d.setEstudianteId(e.getEstudiante().getId());
        d.setEstudianteNombre(e.getEstudiante().getNombre());
        d.setTextoRespuesta(e.getTextoRespuesta());
        d.setNombreArchivo(e.getNombreArchivo());
        d.setTieneArchivo(e.getRutaArchivo() != null);
        d.setFechaEntrega(e.getFechaEntrega());
        d.setCalificada(e.estaCalificada());
        d.setCalificacion(e.getCalificacion());
        d.setRetroalimentacion(e.getRetroalimentacion());
        d.setFechaCalificacion(e.getFechaCalificacion());
        return d;
    }

    public static EvaluacionCursoResponse aEvaluacion(EvaluacionCurso e) {
        EvaluacionCursoResponse d = new EvaluacionCursoResponse();
        d.setId(e.getId());
        d.setCursoId(e.getCurso().getId());
        d.setCursoTitulo(e.getCurso().getTitulo());
        d.setEstudianteId(e.getEstudiante().getId());
        d.setEstudianteNombre(e.getEstudiante().getNombre());
        d.setPuntaje(e.getPuntaje());
        d.setComentario(e.getComentario());
        d.setFecha(e.getFecha());
        return d;
    }

    public static NotificacionResponse aNotificacion(Notificacion n) {
        NotificacionResponse d = new NotificacionResponse();
        d.setId(n.getId());
        d.setDestinatarioId(n.getDestinatario().getId());
        d.setDestinatarioNombre(n.getDestinatario().getNombre());
        if (n.getRemitente() != null) {
            d.setRemitenteId(n.getRemitente().getId());
            d.setRemitenteNombre(n.getRemitente().getNombre());
        }
        d.setTipo(n.getTipo());
        d.setTitulo(n.getTitulo());
        d.setMensaje(n.getMensaje());
        d.setCursoId(n.getCursoId());
        d.setLeida(Boolean.TRUE.equals(n.getLeida()));
        d.setFecha(n.getFecha());
        return d;
    }

    public static ContenidoResponse aContenido(Contenido c) {
        ContenidoResponse d = new ContenidoResponse();
        d.setId(c.getId());
        d.setTitulo(c.getTitulo());
        d.setDescripcion(c.getDescripcion());
        d.setTipo(c.getTipo());
        d.setUrl(c.getUrl());
        d.setTieneArchivo(c.getRutaArchivo() != null);
        d.setNombreArchivo(c.getNombreArchivo());
        d.setTamanoBytes(c.getTamanoBytes());
        d.setFechaPublicacion(c.getFechaPublicacion());
        d.setUnidadId(c.getUnidad().getId());
        d.setUnidadTitulo(c.getUnidad().getTitulo());
        d.setCursoId(c.getUnidad().getCurso().getId());
        return d;
    }

    public static ForoResponse aForo(Foro f, long totalMensajes) {
        ForoResponse d = new ForoResponse();
        d.setId(f.getId());
        d.setCursoId(f.getCurso().getId());
        d.setTitulo(f.getTitulo());
        d.setDescripcion(f.getDescripcion());
        d.setTipo(f.getTipo());
        d.setAbierto(f.estaAbierto());
        d.setCreadorId(f.getCreador().getId());
        d.setCreadorNombre(f.getCreador().getNombre());
        d.setFechaCreacion(f.getFechaCreacion());
        d.setTotalMensajes(totalMensajes);
        return d;
    }

    public static MensajeResponse aMensaje(MensajeForo m) {
        MensajeResponse d = new MensajeResponse();
        d.setId(m.getId());
        d.setForoId(m.getForo().getId());
        d.setAutorId(m.getAutor().getId());
        d.setAutorNombre(m.getAutor().getNombre());
        d.setAutorRol(m.getAutor().getRol().getNombre());
        d.setContenido(m.getContenido());
        d.setRespuestaAId(m.getRespuestaAId());
        d.setFecha(m.getFecha());
        return d;
    }

    public static ReclamoResponse aReclamo(Reclamo r) {
        ReclamoResponse d = new ReclamoResponse();
        d.setId(r.getId());
        d.setEntregaId(r.getEntrega().getId());
        d.setActividadTitulo(r.getEntrega().getActividad().getTitulo());
        d.setCursoId(r.getEntrega().getActividad().getUnidad().getCurso().getId());
        d.setEstudianteId(r.getEstudiante().getId());
        d.setEstudianteNombre(r.getEstudiante().getNombre());
        d.setMotivo(r.getMotivo());
        d.setEstado(r.getEstado());
        d.setRespuesta(r.getRespuesta());
        d.setNotaAnterior(r.getNotaAnterior());
        d.setNotaNueva(r.getNotaNueva());
        if (r.getDocente() != null) {
            d.setDocenteId(r.getDocente().getId());
        }
        d.setFechaCreacion(r.getFechaCreacion());
        d.setFechaRespuesta(r.getFechaRespuesta());
        return d;
    }

    public static CertificadoResponse aCertificado(Certificado c) {
        CertificadoResponse d = new CertificadoResponse();
        d.setId(c.getId());
        d.setCodigo(c.getCodigo());
        d.setEstudianteId(c.getEstudiante().getId());
        d.setEstudianteNombre(c.getEstudiante().getNombre());
        d.setCursoId(c.getCurso().getId());
        d.setCursoTitulo(c.getCurso().getTitulo());
        d.setNotaFinal(c.getNotaFinal());
        d.setEstado(c.getEstado());
        d.setFechaSolicitud(c.getFechaSolicitud());
        d.setFechaEmision(c.getFechaEmision());
        return d;
    }
}
