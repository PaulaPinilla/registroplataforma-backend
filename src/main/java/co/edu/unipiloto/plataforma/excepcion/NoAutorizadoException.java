package co.edu.unipiloto.plataforma.excepcion;

/** Se lanza cuando las credenciales son incorrectas. La API responde 401. */
public class NoAutorizadoException extends RuntimeException {

    public NoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
