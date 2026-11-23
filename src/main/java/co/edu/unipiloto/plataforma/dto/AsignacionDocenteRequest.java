package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class AsignacionDocenteRequest {

    @NotNull(message = "El coordinadorId es obligatorio")
    private Long coordinadorId;

    @NotNull(message = "El docenteId es obligatorio")
    private Long docenteId;

    public Long getCoordinadorId() { return coordinadorId; }
    public void setCoordinadorId(Long coordinadorId) { this.coordinadorId = coordinadorId; }

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

}
