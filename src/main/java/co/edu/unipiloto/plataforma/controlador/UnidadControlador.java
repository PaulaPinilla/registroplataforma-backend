package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.UnidadRequest;
import co.edu.unipiloto.plataforma.dto.UnidadResponse;
import co.edu.unipiloto.plataforma.servicio.UnidadServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/unidades")
public class UnidadControlador {

    private final UnidadServicio servicio;

    public UnidadControlador(UnidadServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<UnidadResponse> listar(@RequestParam(required = false) Long cursoId) {
        return servicio.listar(cursoId);
    }

    @GetMapping("/{id}")
    public UnidadResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnidadResponse crear(@Valid @RequestBody UnidadRequest req) {
        return servicio.crear(req);
    }

    @PutMapping("/{id}")
    public UnidadResponse actualizar(@PathVariable Long id, @Valid @RequestBody UnidadRequest req) {
        return servicio.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}
