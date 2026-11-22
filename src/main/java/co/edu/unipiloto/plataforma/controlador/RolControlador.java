package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.RolRequest;
import co.edu.unipiloto.plataforma.dto.RolResponse;
import co.edu.unipiloto.plataforma.servicio.RolServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolControlador {

    private final RolServicio servicio;

    public RolControlador(RolServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<RolResponse> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public RolResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RolResponse crear(@Valid @RequestBody RolRequest req) {
        return servicio.crear(req);
    }

    @PutMapping("/{id}")
    public RolResponse actualizar(@PathVariable Long id, @Valid @RequestBody RolRequest req) {
        return servicio.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}
