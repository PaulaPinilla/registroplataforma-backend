package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.servicio.ArchivoDescargado;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/** Arma la respuesta HTTP de una descarga de archivo. */
final class Descarga {

    private Descarga() {
    }

    static ResponseEntity<byte[]> de(ArchivoDescargado archivo) {
        String nombre = archivo.getNombre() == null ? "archivo" : archivo.getNombre().replaceAll("[\"\\\\\r\n]", "_");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombre + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(archivo.getContenido());
    }
}
