package co.edu.unipiloto.plataforma.dto;

import java.time.LocalDateTime;

public class CursoResponse {

    private Long id;

    private String titulo;

    private String descripcion;

    private String semestre;

    private LocalDateTime fechaFinalizacion;

    private double pesoCorte1;

    private double pesoCorte2;

    private double pesoCorte3;

    private boolean exigeClave;

    private Long docenteId;

    private String docenteNombre;

    private Long ofertaId;

    private String ofertaNombre;

    private boolean finalizado;

    private String objetivos;

    private String resultadosAprendizaje;

    private Integer cupos;

    private LocalDateTime fechaInicio;

    private boolean publicado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

    public double getPesoCorte1() { return pesoCorte1; }
    public void setPesoCorte1(double pesoCorte1) { this.pesoCorte1 = pesoCorte1; }

    public double getPesoCorte2() { return pesoCorte2; }
    public void setPesoCorte2(double pesoCorte2) { this.pesoCorte2 = pesoCorte2; }

    public double getPesoCorte3() { return pesoCorte3; }
    public void setPesoCorte3(double pesoCorte3) { this.pesoCorte3 = pesoCorte3; }

    public boolean isExigeClave() { return exigeClave; }
    public void setExigeClave(boolean exigeClave) { this.exigeClave = exigeClave; }

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

    public String getDocenteNombre() { return docenteNombre; }
    public void setDocenteNombre(String docenteNombre) { this.docenteNombre = docenteNombre; }

    public Long getOfertaId() { return ofertaId; }
    public void setOfertaId(Long ofertaId) { this.ofertaId = ofertaId; }

    public String getOfertaNombre() { return ofertaNombre; }
    public void setOfertaNombre(String ofertaNombre) { this.ofertaNombre = ofertaNombre; }

    public boolean isFinalizado() { return finalizado; }
    public void setFinalizado(boolean finalizado) { this.finalizado = finalizado; }

    public String getObjetivos() { return objetivos; }
    public void setObjetivos(String objetivos) { this.objetivos = objetivos; }

    public String getResultadosAprendizaje() { return resultadosAprendizaje; }
    public void setResultadosAprendizaje(String resultadosAprendizaje) { this.resultadosAprendizaje = resultadosAprendizaje; }

    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }

}
