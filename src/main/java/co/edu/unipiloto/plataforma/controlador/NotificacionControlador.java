package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.EnvioNotificacionRequest;
import co.edu.unipiloto.plataforma.dto.EnvioNotificacionResponse;
import co.edu.unipiloto.plataforma.dto.NotificacionResponse;
import co.edu.unipiloto.plataforma.servicio.NotificacionServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionControlador {

    private final NotificacionServicio servicio;

    public NotificacionControlador(NotificacionServicio servicio) {
        this.servicio = servicio;
    }

    /** Bandeja de un usuario. Ejemplo: GET /api/notificaciones?usuarioId=3&soloNoLeidas=true */
    @GetMapping
    public List<NotificacionResponse> listar(@RequestParam Long usuarioId,
                                             @RequestParam(defaultValue = "false") boolean soloNoLeidas) {
        return servicio.listar(usuarioId, soloNoLeidas);
    }

    @PutMapping("/{id}/leida")
    public NotificacionResponse marcarLeida(@PathVariable Long id) {
        return servicio.marcarLeida(id);
    }

    @PutMapping("/usuario/{usuarioId}/leer-todas")
    public Map<String, Integer> marcarTodas(@PathVariable Long usuarioId) {
        return Collections.singletonMap("marcadas", servicio.marcarTodasLeidas(usuarioId));
    }

    /** Envio manual: el coordinador a cualquiera, el docente a los estudiantes de su curso. */
    @PostMapping("/enviar")
    @ResponseStatus(HttpStatus.CREATED)
    public EnvioNotificacionResponse enviar(@Valid @RequestBody EnvioNotificacionRequest req) {
        return servicio.enviar(req);
    }
}
