package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class ReclamoRequest {

    @NotNull(message = "El entregaId es obligatorio")
    private Long entregaId;

    @NotNull(message = "El estudianteId es obligatorio")
    private Long estudianteId;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(min = 10, max = 2000, message = "El motivo debe tener entre 10 y 2000 caracteres")
    private String motivo;

    public Long getEntregaId() { return entregaId; }
    public void setEntregaId(Long entregaId) { this.entregaId = entregaId; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

}
