package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class EvaluacionCursoActualizarRequest {

    @NotNull(message = "El puntaje es obligatorio")
    @Min(value = 1, message = "El puntaje minimo es 1")
    @Max(value = 5, message = "El puntaje maximo es 5")
    private Integer puntaje;

    @Size(max = 1000, message = "El comentario admite maximo 1000 caracteres")
    private String comentario;

    public Integer getPuntaje() { return puntaje; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

}
