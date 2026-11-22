package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.OfertaRequest;
import co.edu.unipiloto.plataforma.dto.OfertaResponse;
import co.edu.unipiloto.plataforma.modelo.EstadoOferta;
import co.edu.unipiloto.plataforma.servicio.OfertaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaControlador {

    private final OfertaServicio servicio;

    public OfertaControlador(OfertaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<OfertaResponse> listar(@RequestParam(required = false) EstadoOferta estado) {
        return servicio.listar(estado);
    }

    @GetMapping("/{id}")
    public OfertaResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OfertaResponse crear(@Valid @RequestBody OfertaRequest req) {
        return servicio.crear(req);
    }

    @PutMapping("/{id}")
    public OfertaResponse actualizar(@PathVariable Long id, @Valid @RequestBody OfertaRequest req) {
        return servicio.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}
