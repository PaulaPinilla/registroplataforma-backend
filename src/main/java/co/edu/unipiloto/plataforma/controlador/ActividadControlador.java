package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.ActividadRequest;
import co.edu.unipiloto.plataforma.dto.ActividadResponse;
import co.edu.unipiloto.plataforma.servicio.ActividadServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadControlador {

    private final ActividadServicio servicio;

    public ActividadControlador(ActividadServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ActividadResponse> listar(@RequestParam(required = false) Long unidadId,
                                          @RequestParam(required = false) Long cursoId) {
        return servicio.listar(unidadId, cursoId);
    }

    @GetMapping("/{id}")
    public ActividadResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActividadResponse crear(@Valid @RequestBody ActividadRequest req) {
        return servicio.crear(req);
    }

    @PutMapping("/{id}")
    public ActividadResponse actualizar(@PathVariable Long id, @Valid @RequestBody ActividadRequest req) {
        return servicio.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}
