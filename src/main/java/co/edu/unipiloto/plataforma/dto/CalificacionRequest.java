package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class CalificacionRequest {

    @NotNull(message = "El docenteId es obligatorio")
    private Long docenteId;

    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "0.0", message = "La nota minima es 0.0")
    @DecimalMax(value = "5.0", message = "La nota maxima es 5.0")
    private Double nota;

    @Size(max = 2000, message = "La retroalimentacion admite maximo 2000 caracteres")
    private String retroalimentacion;

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public String getRetroalimentacion() { return retroalimentacion; }
    public void setRetroalimentacion(String retroalimentacion) { this.retroalimentacion = retroalimentacion; }

}
