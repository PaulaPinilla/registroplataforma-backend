package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class MatriculaRequest {

    @NotNull(message = "El estudianteId es obligatorio")
    private Long estudianteId;

    @NotNull(message = "El cursoId es obligatorio")
    private Long cursoId;

    private String clave;

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

}
