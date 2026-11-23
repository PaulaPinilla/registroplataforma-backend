package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.ForoRequest;
import co.edu.unipiloto.plataforma.dto.ForoResponse;
import co.edu.unipiloto.plataforma.dto.MensajeRequest;
import co.edu.unipiloto.plataforma.dto.MensajeResponse;
import co.edu.unipiloto.plataforma.servicio.ForoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/** Foros y chats de un curso. */
@RestController
@RequestMapping("/api/foros")
public class ForoControlador {

    private final ForoServicio servicio;

    public ForoControlador(ForoServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ForoResponse crear(@Valid @RequestBody ForoRequest req) {
        return servicio.crear(req);
    }

    @GetMapping
    public List<ForoResponse> listarPorCurso(@RequestParam Long cursoId) {
        return servicio.listarPorCurso(cursoId);
    }

    @GetMapping("/{id}")
    public ForoResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    /** Abrir o cerrar. Ejemplo: PUT /api/foros/1/estado?docenteId=2&abierto=false */
    @PutMapping("/{id}/estado")
    public ForoResponse cambiarEstado(@PathVariable Long id, @RequestParam Long docenteId, @RequestParam boolean abierto) {
        return servicio.cambiarEstado(id, docenteId, abierto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, @RequestParam Long docenteId) {
        servicio.eliminar(id, docenteId);
    }

    @PostMapping("/{id}/mensajes")
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeResponse publicar(@PathVariable Long id, @Valid @RequestBody MensajeRequest req) {
        return servicio.publicar(id, req);
    }

    /** Ejemplo para el chat: GET /api/foros/1/mensajes?usuarioId=3&despuesDeId=15 devuelve solo los nuevos. */
    @GetMapping("/{id}/mensajes")
    public List<MensajeResponse> mensajes(@PathVariable Long id, @RequestParam Long usuarioId,
                                          @RequestParam(required = false) Long despuesDeId) {
        return servicio.listarMensajes(id, usuarioId, despuesDeId);
    }
}
