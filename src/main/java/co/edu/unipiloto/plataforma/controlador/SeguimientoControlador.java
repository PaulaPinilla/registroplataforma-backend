package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.EstadisticasCursoResponse;
import co.edu.unipiloto.plataforma.dto.EstadisticasGeneralResponse;
import co.edu.unipiloto.plataforma.dto.ProgresoCursoResponse;
import co.edu.unipiloto.plataforma.servicio.EstadisticaServicio;
import co.edu.unipiloto.plataforma.servicio.ProgresoServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Seguimiento de progreso y estadisticas (solo lectura). */
@RestController
@RequestMapping("/api")
public class SeguimientoControlador {

    private final ProgresoServicio progreso;
    private final EstadisticaServicio estadisticas;

    public SeguimientoControlador(ProgresoServicio progreso, EstadisticaServicio estadisticas) {
        this.progreso = progreso;
        this.estadisticas = estadisticas;
    }

    @GetMapping("/seguimiento/cursos/{cursoId}/estudiantes/{estudianteId}")
    public ProgresoCursoResponse progresoDeEstudiante(@PathVariable Long cursoId, @PathVariable Long estudianteId) {
        return progreso.deEstudiante(cursoId, estudianteId);
    }

    @GetMapping("/seguimiento/cursos/{cursoId}")
    public List<ProgresoCursoResponse> progresoDelCurso(@PathVariable Long cursoId) {
        return progreso.deCurso(cursoId);
    }

    @GetMapping("/estadisticas/cursos/{cursoId}")
    public EstadisticasCursoResponse estadisticasDeCurso(@PathVariable Long cursoId) {
        return estadisticas.deCurso(cursoId);
    }

    @GetMapping("/estadisticas/general")
    public EstadisticasGeneralResponse estadisticasGenerales() {
        return estadisticas.general();
    }
}
