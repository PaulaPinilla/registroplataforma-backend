package co.edu.unipiloto.plataforma.dto;

import java.time.LocalDateTime;

public class EntregaResponse {

    private Long id;

    private Long actividadId;

    private String actividadTitulo;

    private Long estudianteId;

    private String estudianteNombre;

    private String textoRespuesta;

    private String nombreArchivo;

    private boolean tieneArchivo;

    private LocalDateTime fechaEntrega;

    private boolean calificada;

    private Double calificacion;

    private String retroalimentacion;

    private LocalDateTime fechaCalificacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }

    public String getActividadTitulo() { return actividadTitulo; }
    public void setActividadTitulo(String actividadTitulo) { this.actividadTitulo = actividadTitulo; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public String getTextoRespuesta() { return textoRespuesta; }
    public void setTextoRespuesta(String textoRespuesta) { this.textoRespuesta = textoRespuesta; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public boolean isTieneArchivo() { return tieneArchivo; }
    public void setTieneArchivo(boolean tieneArchivo) { this.tieneArchivo = tieneArchivo; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public boolean isCalificada() { return calificada; }
    public void setCalificada(boolean calificada) { this.calificada = calificada; }

    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }

    public String getRetroalimentacion() { return retroalimentacion; }
    public void setRetroalimentacion(String retroalimentacion) { this.retroalimentacion = retroalimentacion; }

    public LocalDateTime getFechaCalificacion() { return fechaCalificacion; }
    public void setFechaCalificacion(LocalDateTime fechaCalificacion) { this.fechaCalificacion = fechaCalificacion; }

}
