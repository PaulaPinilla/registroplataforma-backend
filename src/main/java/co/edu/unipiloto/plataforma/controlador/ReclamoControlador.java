package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.ReclamoRequest;
import co.edu.unipiloto.plataforma.dto.ReclamoRespuestaRequest;
import co.edu.unipiloto.plataforma.dto.ReclamoResponse;
import co.edu.unipiloto.plataforma.modelo.EstadoReclamo;
import co.edu.unipiloto.plataforma.servicio.ReclamoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/** Reclamos por evaluacion. */
@RestController
@RequestMapping("/api/reclamos")
public class ReclamoControlador {

    private final ReclamoServicio servicio;

    public ReclamoControlador(ReclamoServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReclamoResponse presentar(@Valid @RequestBody ReclamoRequest req) {
        return servicio.presentar(req);
    }

    /** Respuesta del docente. */
    @PutMapping("/{id}/respuesta")
    public ReclamoResponse responder(@PathVariable Long id, @Valid @RequestBody ReclamoRespuestaRequest req) {
        return servicio.responder(id, req);
    }

    @GetMapping
    public List<ReclamoResponse> listar(@RequestParam(required = false) Long estudianteId,
                                        @RequestParam(required = false) Long cursoId,
                                        @RequestParam(required = false) Long docenteId,
                                        @RequestParam(required = false) EstadoReclamo estado) {
        return servicio.listar(estudianteId, cursoId, docenteId, estado);
    }

    @GetMapping("/{id}")
    public ReclamoResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }
}
