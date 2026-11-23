package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.EstadoReclamo;
import javax.validation.constraints.*;

public class ReclamoRespuestaRequest {

    @NotNull(message = "El docenteId es obligatorio")
    private Long docenteId;

    @NotNull(message = "La decision es obligatoria (ACEPTADO o RECHAZADO)")
    private EstadoReclamo decision;

    @NotBlank(message = "La respuesta es obligatoria")
    @Size(max = 2000, message = "La respuesta admite maximo 2000 caracteres")
    private String respuesta;

    @DecimalMin(value = "0.0", message = "La nota minima es 0.0")
    @DecimalMax(value = "5.0", message = "La nota maxima es 5.0")
    private Double notaNueva;

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

    public EstadoReclamo getDecision() { return decision; }
    public void setDecision(EstadoReclamo decision) { this.decision = decision; }

    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

    public Double getNotaNueva() { return notaNueva; }
    public void setNotaNueva(Double notaNueva) { this.notaNueva = notaNueva; }

}
