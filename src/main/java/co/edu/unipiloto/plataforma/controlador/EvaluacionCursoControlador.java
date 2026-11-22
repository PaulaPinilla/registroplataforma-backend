package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.EvaluacionCursoActualizarRequest;
import co.edu.unipiloto.plataforma.dto.EvaluacionCursoRequest;
import co.edu.unipiloto.plataforma.dto.EvaluacionCursoResponse;
import co.edu.unipiloto.plataforma.servicio.EvaluacionCursoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones-curso")
public class EvaluacionCursoControlador {

    private final EvaluacionCursoServicio servicio;

    public EvaluacionCursoControlador(EvaluacionCursoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<EvaluacionCursoResponse> listar(@RequestParam(required = false) Long cursoId) {
        return servicio.listar(cursoId);
    }

    @GetMapping("/{id}")
    public EvaluacionCursoResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvaluacionCursoResponse crear(@Valid @RequestBody EvaluacionCursoRequest req) {
        return servicio.crear(req);
    }

    @PutMapping("/{id}")
    public EvaluacionCursoResponse actualizar(@PathVariable Long id,
                                              @Valid @RequestBody EvaluacionCursoActualizarRequest req) {
        return servicio.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}
