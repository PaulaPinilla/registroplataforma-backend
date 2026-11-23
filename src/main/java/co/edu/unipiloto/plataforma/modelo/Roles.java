package co.edu.unipiloto.plataforma.modelo;

/** Nombres de los roles base de la plataforma. */
public final class Roles {

    public static final String COORDINADOR = "COORDINADOR";
    public static final String DOCENTE = "DOCENTE";
    public static final String ESTUDIANTE = "ESTUDIANTE";

    private Roles() {
    }

    public static boolean esBase(String nombre) {
        return COORDINADOR.equalsIgnoreCase(nombre) || DOCENTE.equalsIgnoreCase(nombre)
                || ESTUDIANTE.equalsIgnoreCase(nombre);
    }
}
