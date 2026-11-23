package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.DestinoNotificacion;
import javax.validation.constraints.*;

public class EnvioNotificacionRequest {

    @NotNull(message = "El remitenteId es obligatorio")
    private Long remitenteId;

    @NotNull(message = "El destino es obligatorio (USUARIO, DOCENTES, ESTUDIANTES, CURSO o TODOS)")
    private DestinoNotificacion destino;

    private Long usuarioId;

    private Long cursoId;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150, message = "El titulo admite maximo 150 caracteres")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 1000, message = "El mensaje admite maximo 1000 caracteres")
    private String mensaje;

    public Long getRemitenteId() { return remitenteId; }
    public void setRemitenteId(Long remitenteId) { this.remitenteId = remitenteId; }

    public DestinoNotificacion getDestino() { return destino; }
    public void setDestino(DestinoNotificacion destino) { this.destino = destino; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

}
