package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class EntregaRequest {

    @NotNull(message = "El actividadId es obligatorio")
    private Long actividadId;

    @NotNull(message = "El estudianteId es obligatorio")
    private Long estudianteId;

    @Size(max = 4000, message = "La respuesta admite maximo 4000 caracteres")
    private String textoRespuesta;

    @Size(max = 255, message = "El nombre del archivo admite maximo 255 caracteres")
    private String nombreArchivo;

    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getTextoRespuesta() { return textoRespuesta; }
    public void setTextoRespuesta(String textoRespuesta) { this.textoRespuesta = textoRespuesta; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

}
