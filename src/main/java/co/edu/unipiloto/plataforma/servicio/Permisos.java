package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.modelo.Curso;
import co.edu.unipiloto.plataforma.modelo.Usuario;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import org.springframework.stereotype.Component;

/**
 * Reglas de permisos por rol. La API aun no usa tokens: el usuario que actua se indica con su id
 * y aqui se verifica que su rol permita la accion.
 */
@Component
public class Permisos {

    private final Buscador buscar;
    private final MatriculaRepositorio matriculas;

    public Permisos(Buscador buscar, MatriculaRepositorio matriculas) {
        this.buscar = buscar;
        this.matriculas = matriculas;
    }

    /** Devuelve el usuario si tiene el rol pedido y esta activo; si no, 403. */
    public Usuario exigirRol(Long usuarioId, String rol, String accion) {
        Usuario u = buscar.usuario(usuarioId);
        if (!rol.equalsIgnoreCase(u.getRol().getNombre())) {
            throw new ProhibidoException("Solo un usuario con rol " + rol + " puede " + accion);
        }
        if (!u.isActivo()) {
            throw new ProhibidoException("El usuario esta desactivado");
        }
        return u;
    }

    /** Devuelve el curso si el docente indicado es quien lo dicta; si no, 403. */
    public Curso exigirDocenteDelCurso(Long cursoId, Long docenteId, String accion) {
        Curso curso = buscar.curso(cursoId);
        Usuario docente = buscar.usuario(docenteId);
        boolean esDocente = "DOCENTE".equalsIgnoreCase(docente.getRol().getNombre());
        if (!esDocente || !curso.getDocente().getId().equals(docente.getId())) {
            throw new ProhibidoException("Solo el docente de este curso puede " + accion);
        }
        return curso;
    }

    /** true si el usuario es el docente del curso, un coordinador o un estudiante matriculado. */
    public boolean esMiembroDelCurso(Curso curso, Usuario u) {
        String rol = u.getRol().getNombre();
        if ("COORDINADOR".equalsIgnoreCase(rol)) {
            return true;
        }
        if (curso.getDocente().getId().equals(u.getId())) {
            return true;
        }
        return matriculas.existsByEstudiante_IdAndCurso_Id(u.getId(), curso.getId());
    }
}
