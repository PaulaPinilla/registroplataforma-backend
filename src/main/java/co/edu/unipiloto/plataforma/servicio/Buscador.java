package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.*;
import org.springframework.stereotype.Component;

/**
 * Busca entidades por id y lanza 404 si no existen.
 * Evita repetir el mismo "findById ... orElseThrow" en cada servicio.
 */
@Component
public class Buscador {

    private final RolRepositorio roles;
    private final UsuarioRepositorio usuarios;
    private final OfertaEducativaRepositorio ofertas;
    private final CursoRepositorio cursos;
    private final UnidadRepositorio unidades;
    private final ActividadRepositorio actividades;
    private final EntregaRepositorio entregas;
    private final EvaluacionCursoRepositorio evaluaciones;

    public Buscador(RolRepositorio roles, UsuarioRepositorio usuarios, OfertaEducativaRepositorio ofertas,
                    CursoRepositorio cursos, UnidadRepositorio unidades, ActividadRepositorio actividades,
                    EntregaRepositorio entregas, EvaluacionCursoRepositorio evaluaciones) {
        this.roles = roles;
        this.usuarios = usuarios;
        this.ofertas = ofertas;
        this.cursos = cursos;
        this.unidades = unidades;
        this.actividades = actividades;
        this.entregas = entregas;
        this.evaluaciones = evaluaciones;
    }

    public Rol rol(Long id) {
        return roles.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("El rol", id));
    }

    public Usuario usuario(Long id) {
        return usuarios.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("El usuario", id));
    }

    public OfertaEducativa oferta(Long id) {
        return ofertas.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("La oferta", id));
    }

    public Curso curso(Long id) {
        return cursos.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("El curso", id));
    }

    public Unidad unidad(Long id) {
        return unidades.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("La unidad", id));
    }

    public Actividad actividad(Long id) {
        return actividades.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("La actividad", id));
    }

    public Entrega entrega(Long id) {
        return entregas.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("La entrega", id));
    }

    public EvaluacionCurso evaluacion(Long id) {
        return evaluaciones.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("La evaluacion", id));
    }
}
