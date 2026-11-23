package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class MensajeRequest {

    @NotNull(message = "El autorId es obligatorio")
    private Long autorId;

    @NotBlank(message = "El mensaje no puede estar vacio")
    @Size(max = 2000, message = "El mensaje admite maximo 2000 caracteres")
    private String contenido;

    private Long respuestaAId;

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Long getRespuestaAId() { return respuestaAId; }
    public void setRespuestaAId(Long respuestaAId) { this.respuestaAId = respuestaAId; }

}
