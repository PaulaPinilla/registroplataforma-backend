package co.edu.unipiloto.plataforma.dto;

public class LoginResponse {

    private String mensaje;

    private UsuarioResponse usuario;

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public UsuarioResponse getUsuario() { return usuario; }
    public void setUsuario(UsuarioResponse usuario) { this.usuario = usuario; }

}
