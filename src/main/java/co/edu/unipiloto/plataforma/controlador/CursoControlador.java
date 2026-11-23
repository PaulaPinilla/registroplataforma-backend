package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.AsignacionDocenteRequest;
import co.edu.unipiloto.plataforma.dto.ConfiguracionCursoResponse;
import co.edu.unipiloto.plataforma.dto.CursoRequest;
import co.edu.unipiloto.plataforma.dto.CursoResponse;
import co.edu.unipiloto.plataforma.dto.PublicacionCursoRequest;
import co.edu.unipiloto.plataforma.servicio.CursoServicio;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoControlador {

    private final CursoServicio servicio;

    public CursoControlador(CursoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<CursoResponse> listar(@RequestParam(required = false) Long docenteId,
                                      @RequestParam(required = false) Long ofertaId) {
        return servicio.listar(docenteId, ofertaId);
    }

    @GetMapping("/{id}")
    public CursoResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoResponse crear(@Valid @RequestBody CursoRequest req) {
        return servicio.crear(req);
    }

    @PutMapping("/{id}")
    public CursoResponse actualizar(@PathVariable Long id, @Valid @RequestBody CursoRequest req) {
        return servicio.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }

    /** Ejemplo: POST /api/cursos/1/duplicar?semestre=2027-1&fechaFinalizacion=2027-06-30T23:59:00 */
    @PostMapping("/{id}/duplicar")
    @ResponseStatus(HttpStatus.CREATED)
    public CursoResponse duplicar(@PathVariable Long id,
                                  @RequestParam String semestre,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFinalizacion) {
        return servicio.duplicar(id, semestre, fechaFinalizacion);
    }

    /** Docente: que le falta al curso (secciones, porcentajes, contenidos) para estar completo. */
    @GetMapping("/{id}/configuracion")
    public ConfiguracionCursoResponse configuracion(@PathVariable Long id) {
        return servicio.configuracion(id);
    }

    /** Coordinador: fija objetivos, resultados, cupos y fechas, y publica el curso (notifica al docente y estudiantes). */
    @PutMapping("/{id}/publicacion")
    public CursoResponse publicar(@PathVariable Long id, @Valid @RequestBody PublicacionCursoRequest req) {
        return servicio.publicar(id, req);
    }

    /** Coordinador: retira el curso del catalogo (solo si no tiene matriculados). */
    @PutMapping("/{id}/despublicar")
    public CursoResponse despublicar(@PathVariable Long id, @RequestParam Long coordinadorId) {
        return servicio.despublicar(id, coordinadorId);
    }

    /** Coordinador: asigna el docente del curso y le envia una notificacion. */
    @PutMapping("/{id}/docente")
    public CursoResponse asignarDocente(@PathVariable Long id, @Valid @RequestBody AsignacionDocenteRequest req) {
        return servicio.asignarDocente(id, req);
    }
}
