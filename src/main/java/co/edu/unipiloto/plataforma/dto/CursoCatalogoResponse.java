package co.edu.unipiloto.plataforma.dto;

import java.time.LocalDateTime;

public class CursoCatalogoResponse {

    private Long id;

    private String titulo;

    private String descripcion;

    private String objetivos;

    private String resultadosAprendizaje;

    private String docenteNombre;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFinalizacion;

    private Integer cupos;

    private long matriculados;

    private long cuposDisponibles;

    private boolean exigeClave;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getObjetivos() { return objetivos; }
    public void setObjetivos(String objetivos) { this.objetivos = objetivos; }

    public String getResultadosAprendizaje() { return resultadosAprendizaje; }
    public void setResultadosAprendizaje(String resultadosAprendizaje) { this.resultadosAprendizaje = resultadosAprendizaje; }

    public String getDocenteNombre() { return docenteNombre; }
    public void setDocenteNombre(String docenteNombre) { this.docenteNombre = docenteNombre; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }

    public long getMatriculados() { return matriculados; }
    public void setMatriculados(long matriculados) { this.matriculados = matriculados; }

    public long getCuposDisponibles() { return cuposDisponibles; }
    public void setCuposDisponibles(long cuposDisponibles) { this.cuposDisponibles = cuposDisponibles; }

    public boolean isExigeClave() { return exigeClave; }
    public void setExigeClave(boolean exigeClave) { this.exigeClave = exigeClave; }

}
