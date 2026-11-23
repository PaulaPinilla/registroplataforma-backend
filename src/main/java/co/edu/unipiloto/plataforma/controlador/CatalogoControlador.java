package co.edu.unipiloto.plataforma.controlador;

import co.edu.unipiloto.plataforma.dto.CursoCatalogoResponse;
import co.edu.unipiloto.plataforma.servicio.CursoServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Cursos publicados por el coordinador, visibles para docentes y estudiantes. */
@RestController
@RequestMapping("/api/catalogo")
public class CatalogoControlador {

    private final CursoServicio servicio;

    public CatalogoControlador(CursoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<CursoCatalogoResponse> cursosDisponibles() {
        return servicio.catalogo();
    }
}
