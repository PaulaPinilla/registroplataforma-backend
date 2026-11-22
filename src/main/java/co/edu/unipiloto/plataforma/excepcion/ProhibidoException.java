package co.edu.unipiloto.plataforma.excepcion;

/** Se lanza cuando el usuario no tiene permiso para la operacion (p. ej. un estudiante intenta calificar). La API responde 403. */
public class ProhibidoException extends RuntimeException {

    public ProhibidoException(String mensaje) {
        super(mensaje);
    }
}
