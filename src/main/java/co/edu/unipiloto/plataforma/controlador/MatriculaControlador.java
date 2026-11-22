package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.MatriculaRequest;
import co.edu.unipiloto.plataforma.dto.MatriculaResponse;
import co.edu.unipiloto.plataforma.servicio.MatriculaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaControlador {

    private final MatriculaServicio servicio;

    public MatriculaControlador(MatriculaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<MatriculaResponse> listar(@RequestParam(required = false) Long estudianteId,
                                          @RequestParam(required = false) Long cursoId) {
        return servicio.listar(estudianteId, cursoId);
    }

    @GetMapping("/{id}")
    public MatriculaResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatriculaResponse matricular(@Valid @RequestBody MatriculaRequest req) {
        return servicio.matricular(req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long id) {
        servicio.cancelar(id);
    }
}
