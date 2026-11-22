package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.LoginRequest;
import co.edu.unipiloto.plataforma.dto.LoginResponse;
import co.edu.unipiloto.plataforma.dto.UsuarioRequest;
import co.edu.unipiloto.plataforma.dto.UsuarioResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.NoAutorizadoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.Usuario;
import co.edu.unipiloto.plataforma.repositorio.CursoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.EntregaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.EvaluacionCursoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.UsuarioRepositorio;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** CRUD de usuarios (docentes y estudiantes) e inicio de sesion. */
@Service
public class UsuarioServicio {

    private final UsuarioRepositorio usuarios;
    private final CursoRepositorio cursos;
    private final MatriculaRepositorio matriculas;
    private final EntregaRepositorio entregas;
    private final EvaluacionCursoRepositorio evaluaciones;
    private final Buscador buscar;
    private final BCryptPasswordEncoder codificador;

    public UsuarioServicio(UsuarioRepositorio usuarios, CursoRepositorio cursos, MatriculaRepositorio matriculas,
                           EntregaRepositorio entregas, EvaluacionCursoRepositorio evaluaciones,
                           Buscador buscar, BCryptPasswordEncoder codificador) {
        this.usuarios = usuarios;
        this.cursos = cursos;
        this.matriculas = matriculas;
        this.entregas = entregas;
        this.evaluaciones = evaluaciones;
        this.buscar = buscar;
        this.codificador = codificador;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar(Long rolId) {
        List<Usuario> base = rolId == null ? usuarios.findAll() : usuarios.findByRol_Id(rolId);
        List<UsuarioResponse> salida = new ArrayList<>();
        for (Usuario u : base) {
            salida.add(Conversor.aUsuario(u));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long id) {
        return Conversor.aUsuario(buscar.usuario(id));
    }

    @Transactional
    public UsuarioResponse crear(UsuarioRequest req) {
        if (req.getContrasena() == null || req.getContrasena().trim().isEmpty()) {
            throw new ReglaNegocioException("La contrasena es obligatoria al crear un usuario");
        }
        String correo = req.getCorreo().trim().toLowerCase();
        if (usuarios.existsByCorreoIgnoreCase(correo)) {
            throw new ConflictoException("Ya existe un usuario con el correo " + correo);
        }
        Usuario u = new Usuario();
        u.setNombre(req.getNombre().trim());
        u.setCorreo(correo);
        u.setContrasena(codificador.encode(req.getContrasena()));
        u.setRol(buscar.rol(req.getRolId()));
        u.setActivo(req.getActivo() == null || req.getActivo());
        u.setFechaRegistro(LocalDateTime.now());
        return Conversor.aUsuario(usuarios.save(u));
    }

    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest req) {
        Usuario u = buscar.usuario(id);
        String correo = req.getCorreo().trim().toLowerCase();
        if (!u.getCorreo().equalsIgnoreCase(correo) && usuarios.existsByCorreoIgnoreCase(correo)) {
            throw new ConflictoException("Ya existe un usuario con el correo " + correo);
        }
        u.setNombre(req.getNombre().trim());
        u.setCorreo(correo);
        if (req.getContrasena() != null && !req.getContrasena().trim().isEmpty()) {
            u.setContrasena(codificador.encode(req.getContrasena()));
        }
        u.setRol(buscar.rol(req.getRolId()));
        if (req.getActivo() != null) {
            u.setActivo(req.getActivo());
        }
        return Conversor.aUsuario(usuarios.save(u));
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario u = buscar.usuario(id);
        if (cursos.existsByDocente_Id(id)) {
            throw new ConflictoException("No se puede eliminar: el usuario es docente de uno o mas cursos. "
                    + "Puede desactivarlo con activo=false");
        }
        if (matriculas.existsByEstudiante_Id(id) || entregas.existsByEstudiante_Id(id)
                || evaluaciones.existsByEstudiante_Id(id)) {
            throw new ConflictoException("No se puede eliminar: el usuario tiene matriculas, entregas o evaluaciones. "
                    + "Puede desactivarlo con activo=false");
        }
        usuarios.delete(u);
    }

    @Transactional(readOnly = true)
    public LoginResponse iniciarSesion(LoginRequest req) {
        Usuario u = usuarios.findByCorreoIgnoreCase(req.getCorreo().trim())
                .orElseThrow(() -> new NoAutorizadoException("Correo o contrasena incorrectos"));
        if (!codificador.matches(req.getContrasena(), u.getContrasena())) {
            throw new NoAutorizadoException("Correo o contrasena incorrectos");
        }
        if (!u.isActivo()) {
            throw new NoAutorizadoException("El usuario esta desactivado");
        }
        LoginResponse r = new LoginResponse();
        r.setMensaje("Bienvenido " + u.getNombre());
        r.setUsuario(Conversor.aUsuario(u));
        return r;
    }
}
