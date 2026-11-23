package co.edu.unipiloto.plataforma.dto;

import java.util.List;
import javax.validation.constraints.*;

public class ProcesoRequest {

    @NotNull(message = "El cursoId es obligatorio")
    private Long cursoId;

    @NotNull(message = "El docenteId es obligatorio")
    private Long docenteId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre admite maximo 150 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripcion admite maximo 1000 caracteres")
    private String descripcion;

    @NotNull(message = "Las actividades son obligatorias")
    @Size(min = 2, message = "Un proceso necesita al menos 2 actividades en secuencia")
    private List<Long> actividadIds;

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Long> getActividadIds() { return actividadIds; }
    public void setActividadIds(List<Long> actividadIds) { this.actividadIds = actividadIds; }

}
