package co.edu.unipiloto.plataforma.excepcion;

/** Se lanza cuando la peticion incumple una regla del negocio. La API responde 400. */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
