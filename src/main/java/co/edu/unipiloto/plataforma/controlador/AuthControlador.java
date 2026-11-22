package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.LoginRequest;
import co.edu.unipiloto.plataforma.dto.LoginResponse;
import co.edu.unipiloto.plataforma.servicio.UsuarioServicio;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthControlador {

    private final UsuarioServicio servicio;

    public AuthControlador(UsuarioServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return servicio.iniciarSesion(req);
    }
}
