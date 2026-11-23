package co.edu.unipiloto.plataforma.dto;

public class EnvioNotificacionResponse {

    private int destinatarios;

    private String mensaje;

    public int getDestinatarios() { return destinatarios; }
    public void setDestinatarios(int destinatarios) { this.destinatarios = destinatarios; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

}
