package co.edu.unipiloto.plataforma.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.*;

public class CursoRequest {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150, message = "El titulo admite maximo 150 caracteres")
    private String titulo;

    @Size(max = 1000, message = "La descripcion admite maximo 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "El semestre es obligatorio")
    @Size(max = 20, message = "El semestre admite maximo 20 caracteres")
    private String semestre;

    @NotNull(message = "La fecha de finalizacion es obligatoria")
    private LocalDateTime fechaFinalizacion;

    @NotNull(message = "El peso del corte 1 es obligatorio")
    @DecimalMin(value = "0", message = "El peso del corte 1 no puede ser negativo")
    @DecimalMax(value = "100", message = "El peso del corte 1 no puede superar 100")
    private Double pesoCorte1;

    @NotNull(message = "El peso del corte 2 es obligatorio")
    @DecimalMin(value = "0", message = "El peso del corte 2 no puede ser negativo")
    @DecimalMax(value = "100", message = "El peso del corte 2 no puede superar 100")
    private Double pesoCorte2;

    @NotNull(message = "El peso del corte 3 es obligatorio")
    @DecimalMin(value = "0", message = "El peso del corte 3 no puede ser negativo")
    @DecimalMax(value = "100", message = "El peso del corte 3 no puede superar 100")
    private Double pesoCorte3;

    @Size(max = 100, message = "La clave admite maximo 100 caracteres")
    private String claveMatricula;

    @NotNull(message = "El docenteId es obligatorio")
    private Long docenteId;

    private Long ofertaId;

    @Size(max = 2000, message = "Los objetivos admiten maximo 2000 caracteres")
    private String objetivos;

    @Size(max = 2000, message = "Los resultados admiten maximo 2000 caracteres")
    private String resultadosAprendizaje;

    @Min(value = 1, message = "Debe haber al menos 1 cupo")
    private Integer cupos;

    private LocalDateTime fechaInicio;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

    public Double getPesoCorte1() { return pesoCorte1; }
    public void setPesoCorte1(Double pesoCorte1) { this.pesoCorte1 = pesoCorte1; }

    public Double getPesoCorte2() { return pesoCorte2; }
    public void setPesoCorte2(Double pesoCorte2) { this.pesoCorte2 = pesoCorte2; }

    public Double getPesoCorte3() { return pesoCorte3; }
    public void setPesoCorte3(Double pesoCorte3) { this.pesoCorte3 = pesoCorte3; }

    public String getClaveMatricula() { return claveMatricula; }
    public void setClaveMatricula(String claveMatricula) { this.claveMatricula = claveMatricula; }

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

    public Long getOfertaId() { return ofertaId; }
    public void setOfertaId(Long ofertaId) { this.ofertaId = ofertaId; }

    public String getObjetivos() { return objetivos; }
    public void setObjetivos(String objetivos) { this.objetivos = objetivos; }

    public String getResultadosAprendizaje() { return resultadosAprendizaje; }
    public void setResultadosAprendizaje(String resultadosAprendizaje) { this.resultadosAprendizaje = resultadosAprendizaje; }

    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

}
