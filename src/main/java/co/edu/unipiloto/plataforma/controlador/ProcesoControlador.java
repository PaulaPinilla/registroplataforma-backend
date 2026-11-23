package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.ProcesoProgresoResponse;
import co.edu.unipiloto.plataforma.dto.ProcesoRequest;
import co.edu.unipiloto.plataforma.dto.ProcesoResponse;
import co.edu.unipiloto.plataforma.servicio.ProcesoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/** Procesos de formacion: actividades de un curso que se entregan en secuencia. */
@RestController
@RequestMapping("/api/procesos")
public class ProcesoControlador {

    private final ProcesoServicio servicio;

    public ProcesoControlador(ProcesoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ProcesoResponse> listar(@RequestParam(required = false) Long cursoId) {
        return servicio.listar(cursoId);
    }

    @GetMapping("/{id}")
    public ProcesoResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProcesoResponse crear(@Valid @RequestBody ProcesoRequest req) {
        return servicio.crear(req);
    }

    @PutMapping("/{id}")
    public ProcesoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProcesoRequest req) {
        return servicio.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, @RequestParam Long docenteId) {
        servicio.eliminar(id, docenteId);
    }

    /** Ejemplo: GET /api/procesos/1/progreso?estudianteId=3 */
    @GetMapping("/{id}/progreso")
    public ProcesoProgresoResponse progreso(@PathVariable Long id, @RequestParam Long estudianteId) {
        return servicio.progreso(id, estudianteId);
    }
}
