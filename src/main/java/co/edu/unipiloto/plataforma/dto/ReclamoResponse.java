package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.EstadoReclamo;
import java.time.LocalDateTime;

public class ReclamoResponse {

    private Long id;

    private Long entregaId;

    private String actividadTitulo;

    private Long cursoId;

    private Long estudianteId;

    private String estudianteNombre;

    private String motivo;

    private EstadoReclamo estado;

    private String respuesta;

    private Double notaAnterior;

    private Double notaNueva;

    private Long docenteId;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaRespuesta;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEntregaId() { return entregaId; }
    public void setEntregaId(Long entregaId) { this.entregaId = entregaId; }

    public String getActividadTitulo() { return actividadTitulo; }
    public void setActividadTitulo(String actividadTitulo) { this.actividadTitulo = actividadTitulo; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public EstadoReclamo getEstado() { return estado; }
    public void setEstado(EstadoReclamo estado) { this.estado = estado; }

    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

    public Double getNotaAnterior() { return notaAnterior; }
    public void setNotaAnterior(Double notaAnterior) { this.notaAnterior = notaAnterior; }

    public Double getNotaNueva() { return notaNueva; }
    public void setNotaNueva(Double notaNueva) { this.notaNueva = notaNueva; }

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(LocalDateTime fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }

}
