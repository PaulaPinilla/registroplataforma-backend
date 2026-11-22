package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class EntregaActualizarRequest {

    @Size(max = 4000, message = "La respuesta admite maximo 4000 caracteres")
    private String textoRespuesta;

    @Size(max = 255, message = "El nombre del archivo admite maximo 255 caracteres")
    private String nombreArchivo;

    public String getTextoRespuesta() { return textoRespuesta; }
    public void setTextoRespuesta(String textoRespuesta) { this.textoRespuesta = textoRespuesta; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

}
