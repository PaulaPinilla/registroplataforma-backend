package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.TipoForo;
import javax.validation.constraints.*;

public class ForoRequest {

    @NotNull(message = "El cursoId es obligatorio")
    private Long cursoId;

    @NotNull(message = "El docenteId es obligatorio")
    private Long docenteId;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150, message = "El titulo admite maximo 150 caracteres")
    private String titulo;

    @Size(max = 1000, message = "La descripcion admite maximo 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El tipo es obligatorio (FORO o CHAT)")
    private TipoForo tipo;

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public TipoForo getTipo() { return tipo; }
    public void setTipo(TipoForo tipo) { this.tipo = tipo; }

}
