package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Guarda y lee archivos (entregas de estudiantes y materiales del docente) en una carpeta del servidor.
 * Seguridad: el nombre guardado lo genera el servidor (UUID), solo se admiten extensiones conocidas
 * y nunca se usa el nombre que envia el cliente para construir rutas.
 */
@Service
public class ArchivoServicio {

    private static final Set<String> EXTENSIONES = new HashSet<>(Arrays.asList(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "csv", "md", "json", "sql", "java", "py",
            "png", "jpg", "jpeg", "gif", "zip", "rar", "7z", "mp3", "mp4"));

    private final Path base;

    public ArchivoServicio(@Value("${plataforma.archivos.directorio:./archivos}") String directorio) {
        this.base = Paths.get(directorio).toAbsolutePath().normalize();
    }

    /** Nombre original sin rutas ni caracteres raros, para mostrarlo y usarlo en la descarga. */
    public String nombreSeguro(String original) {
        if (original == null || original.trim().isEmpty()) {
            throw new ReglaNegocioException("El archivo no tiene nombre");
        }
        String nombre = original.replace('\\', '/');
        nombre = nombre.substring(nombre.lastIndexOf('/') + 1).trim();
        nombre = nombre.replaceAll("[^A-Za-z0-9._ -]", "_");
        if (nombre.isEmpty() || nombre.length() > 200) {
            throw new ReglaNegocioException("El nombre del archivo no es valido");
        }
        return nombre;
    }

    /** Guarda el archivo en la subcarpeta indicada y devuelve su ruta relativa (subcarpeta/uuid.ext). */
    public String guardar(String subcarpeta, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ReglaNegocioException("Debe adjuntar un archivo (campo 'archivo')");
        }
        String nombre = nombreSeguro(archivo.getOriginalFilename());
        int punto = nombre.lastIndexOf('.');
        String extension = punto < 0 ? "" : nombre.substring(punto + 1).toLowerCase(Locale.ROOT);
        if (!EXTENSIONES.contains(extension)) {
            throw new ReglaNegocioException("Tipo de archivo no permitido. Extensiones validas: " + EXTENSIONES);
        }
        String relativa = subcarpeta + "/" + UUID.randomUUID().toString() + "." + extension;
        try {
            Path destino = resolver(relativa);
            Files.createDirectories(destino.getParent());
            Files.write(destino, archivo.getBytes());
        } catch (IOException e) {
            throw new ReglaNegocioException("No se pudo guardar el archivo: " + e.getMessage());
        }
        return relativa;
    }

    public byte[] leer(String relativa) {
        try {
            Path ruta = resolver(relativa);
            if (!Files.exists(ruta)) {
                throw new RecursoNoEncontradoException("El archivo ya no existe en el servidor");
            }
            return Files.readAllBytes(ruta);
        } catch (IOException e) {
            throw new ReglaNegocioException("No se pudo leer el archivo: " + e.getMessage());
        }
    }

    /** Borra el archivo si existe; nunca lanza error (limpieza de mejor esfuerzo). */
    public void eliminar(String relativa) {
        if (relativa == null) {
            return;
        }
        try {
            Files.deleteIfExists(resolver(relativa));
        } catch (IOException | RuntimeException e) {
            // se ignora: un archivo huerfano no debe impedir el borrado de datos
        }
    }

    private Path resolver(String relativa) {
        Path ruta = base.resolve(relativa).normalize();
        if (!ruta.startsWith(base)) {
            throw new ReglaNegocioException("Ruta de archivo invalida");
        }
        return ruta;
    }
}
