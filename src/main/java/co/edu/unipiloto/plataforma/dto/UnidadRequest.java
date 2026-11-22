package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class UnidadRequest {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150, message = "El titulo admite maximo 150 caracteres")
    private String titulo;

    @Min(value = 1, message = "El orden debe ser 1 o mayor")
    private Integer orden;

    @NotNull(message = "El cursoId es obligatorio")
    private Long cursoId;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

}
