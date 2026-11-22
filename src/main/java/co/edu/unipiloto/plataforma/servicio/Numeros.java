package co.edu.unipiloto.plataforma.servicio;

import java.util.List;

/** Utilidades numericas para notas, promedios y porcentajes. */
public final class Numeros {

    private Numeros() {
    }

    /** Redondea a 2 decimales. */
    public static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    /** Promedio redondeado a 2 decimales, o null si la lista esta vacia. */
    public static Double promedio(List<Double> valores) {
        if (valores == null || valores.isEmpty()) {
            return null;
        }
        double suma = 0;
        for (Double v : valores) {
            suma += v;
        }
        return redondear(suma / valores.size());
    }
}
