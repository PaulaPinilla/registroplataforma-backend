package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.CertificadoRequest;
import co.edu.unipiloto.plataforma.dto.CertificadoResponse;
import co.edu.unipiloto.plataforma.dto.VerificacionCertificadoResponse;
import co.edu.unipiloto.plataforma.servicio.CertificadoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/** Solicitud, consulta y verificacion de certificados. */
@RestController
@RequestMapping("/api/certificados")
public class CertificadoControlador {

    private final CertificadoServicio servicio;

    public CertificadoControlador(CertificadoServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificadoResponse solicitar(@Valid @RequestBody CertificadoRequest req) {
        return servicio.solicitar(req);
    }

    @GetMapping
    public List<CertificadoResponse> listar(@RequestParam(required = false) Long estudianteId,
                                            @RequestParam(required = false) Long cursoId) {
        return servicio.listar(estudianteId, cursoId);
    }

    @GetMapping("/{id}")
    public CertificadoResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    /** Constancia en texto plano. */
    @GetMapping(value = "/{id}/documento", produces = "text/plain;charset=UTF-8")
    public String documento(@PathVariable Long id) {
        return servicio.documento(id);
    }

    /** Verificacion publica por codigo. */
    @GetMapping("/verificar/{codigo}")
    public VerificacionCertificadoResponse verificar(@PathVariable String codigo) {
        return servicio.verificar(codigo);
    }
}
