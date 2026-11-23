package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.ContenidoRequest;
import co.edu.unipiloto.plataforma.dto.ContenidoResponse;
import co.edu.unipiloto.plataforma.servicio.ContenidoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/** Contenidos (materiales) de las unidades: el docente los publica, los estudiantes los consultan y descargan. */
@RestController
@RequestMapping("/api/contenidos")
public class ContenidoControlador {

    private final ContenidoServicio servicio;

    public ContenidoControlador(ContenidoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ContenidoResponse> listar(@RequestParam(required = false) Long unidadId,
                                          @RequestParam(required = false) Long cursoId) {
        return servicio.listar(unidadId, cursoId);
    }

    @GetMapping("/{id}")
    public ContenidoResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContenidoResponse crear(@Valid @RequestBody ContenidoRequest req) {
        return servicio.crear(req);
    }

    @PutMapping("/{id}")
    public ContenidoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ContenidoRequest req) {
        return servicio.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, @RequestParam Long docenteId) {
        servicio.eliminar(id, docenteId);
    }

    /** Form-data: docenteId (texto) y archivo (File). */
    @PostMapping("/{id}/archivo")
    public ContenidoResponse subirArchivo(@PathVariable Long id, @RequestParam Long docenteId,
                                          @RequestParam("archivo") MultipartFile archivo) {
        return servicio.subirArchivo(id, docenteId, archivo);
    }

    /** Descarga del material. Ejemplo: GET /api/contenidos/1/archivo?usuarioId=3 */
    @GetMapping("/{id}/archivo")
    public ResponseEntity<byte[]> descargar(@PathVariable Long id, @RequestParam Long usuarioId) {
        return Descarga.de(servicio.descargar(id, usuarioId));
    }
}
