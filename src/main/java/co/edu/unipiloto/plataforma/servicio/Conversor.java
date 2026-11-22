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
}
