package co.edu.unipiloto.plataforma.excepcion;

/** Se lanza cuando un recurso no existe. La API responde 404. */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public static RecursoNoEncontradoException de(String recurso, Long id) {
        return new RecursoNoEncontradoException(recurso + " con id " + id + " no existe");
    }
}
