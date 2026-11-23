package co.edu.unipiloto.plataforma.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.*;

public class PublicacionCursoRequest {

    @NotNull(message = "El coordinadorId es obligatorio")
    private Long coordinadorId;

    @NotBlank(message = "Los objetivos son obligatorios")
    @Size(max = 2000, message = "Los objetivos admiten maximo 2000 caracteres")
    private String objetivos;

    @NotBlank(message = "Los resultados de aprendizaje son obligatorios")
    @Size(max = 2000, message = "Los resultados admiten maximo 2000 caracteres")
    private String resultadosAprendizaje;

    @NotNull(message = "Los cupos son obligatorios")
    @Min(value = 1, message = "Debe haber al menos 1 cupo")
    private Integer cupos;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de finalizacion es obligatoria")
    private LocalDateTime fechaFinalizacion;

    public Long getCoordinadorId() { return coordinadorId; }
    public void setCoordinadorId(Long coordinadorId) { this.coordinadorId = coordinadorId; }

    public String getObjetivos() { return objetivos; }
    public void setObjetivos(String objetivos) { this.objetivos = objetivos; }

    public String getResultadosAprendizaje() { return resultadosAprendizaje; }
    public void setResultadosAprendizaje(String resultadosAprendizaje) { this.resultadosAprendizaje = resultadosAprendizaje; }

    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

}
