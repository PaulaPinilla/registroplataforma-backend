package co.edu.unipiloto.plataforma.excepcion;

/** Se lanza cuando la operacion choca con el estado actual de los datos (duplicados, etc). La API responde 409. */
public class ConflictoException extends RuntimeException {

    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
