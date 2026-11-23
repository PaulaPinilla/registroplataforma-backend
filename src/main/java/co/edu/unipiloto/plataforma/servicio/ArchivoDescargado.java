package co.edu.unipiloto.plataforma.servicio;

/** Archivo listo para enviarse al cliente: nombre original y contenido. */
public class ArchivoDescargado {

    private final String nombre;
    private final byte[] contenido;

    public ArchivoDescargado(String nombre, byte[] contenido) {
        this.nombre = nombre;
        this.contenido = contenido;
    }

    public String getNombre() {
        return nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }
}
