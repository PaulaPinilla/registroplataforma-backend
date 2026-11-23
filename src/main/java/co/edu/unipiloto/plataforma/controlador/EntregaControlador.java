package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.CalificacionRequest;
import co.edu.unipiloto.plataforma.dto.EntregaActualizarRequest;
import co.edu.unipiloto.plataforma.dto.EntregaRequest;
import co.edu.unipiloto.plataforma.dto.EntregaResponse;
import co.edu.unipiloto.plataforma.servicio.EntregaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/entregas")
public class EntregaControlador {

    private final EntregaServicio servicio;

    public EntregaControlador(EntregaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<EntregaResponse> listar(@RequestParam(required = false) Long actividadId,
                                        @RequestParam(required = false) Long estudianteId,
                                        @RequestParam(required = false) Long cursoId) {
        return servicio.listar(actividadId, estudianteId, cursoId);
    }

    @GetMapping("/{id}")
    public EntregaResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntregaResponse entregar(@Valid @RequestBody EntregaRequest req) {
        return servicio.entregar(req);
    }

    @PutMapping("/{id}")
    public EntregaResponse modificar(@PathVariable Long id, @Valid @RequestBody EntregaActualizarRequest req) {
        return servicio.modificar(id, req);
    }

    /** Calificacion y retroalimentacion del docente. */
    @PutMapping("/{id}/calificacion")
    public EntregaResponse calificar(@PathVariable Long id, @Valid @RequestBody CalificacionRequest req) {
        return servicio.calificar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }

    /** Entrega con archivo (form-data): actividadId, estudianteId, archivo (File) y, opcional, textoRespuesta. */
    @PostMapping("/archivo")
    @ResponseStatus(HttpStatus.CREATED)
    public EntregaResponse entregarConArchivo(@RequestParam Long actividadId, @RequestParam Long estudianteId,
                                              @RequestParam(required = false) String textoRespuesta,
                                              @RequestParam("archivo") MultipartFile archivo) {
        return servicio.entregarConArchivo(actividadId, estudianteId, textoRespuesta, archivo);
    }

    /** El estudiante reemplaza su archivo antes de ser calificado (form-data: estudianteId y archivo). */
    @PutMapping("/{id}/archivo")
    public EntregaResponse reemplazarArchivo(@PathVariable Long id, @RequestParam Long estudianteId,
                                             @RequestParam("archivo") MultipartFile archivo) {
        return servicio.reemplazarArchivo(id, estudianteId, archivo);
    }

    /** Descarga del archivo entregado. Ejemplo: GET /api/entregas/1/archivo?usuarioId=2 */
    @GetMapping("/{id}/archivo")
    public ResponseEntity<byte[]> descargarArchivo(@PathVariable Long id, @RequestParam Long usuarioId) {
        return Descarga.de(servicio.descargarArchivo(id, usuarioId));
    }
}
