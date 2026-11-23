package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.ReportePlataformaResponse;
import co.edu.unipiloto.plataforma.servicio.ReporteServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Reportes para el coordinador. */
@RestController
@RequestMapping("/api/reportes")
public class ReporteControlador {

    private final ReporteServicio servicio;

    public ReporteControlador(ReporteServicio servicio) {
        this.servicio = servicio;
    }

    /** Ejemplo: GET /api/reportes/plataforma?coordinadorId=1 */
    @GetMapping("/plataforma")
    public ReportePlataformaResponse plataforma(@RequestParam Long coordinadorId) {
        return servicio.plataforma(coordinadorId);
    }
}
