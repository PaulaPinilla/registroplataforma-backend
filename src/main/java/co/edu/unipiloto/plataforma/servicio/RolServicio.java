package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.RolRequest;
import co.edu.unipiloto.plataforma.dto.RolResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.modelo.Rol;
import co.edu.unipiloto.plataforma.modelo.Roles;
import co.edu.unipiloto.plataforma.repositorio.RolRepositorio;
import co.edu.unipiloto.plataforma.repositorio.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/** CRUD de roles. Los roles base (DOCENTE y ESTUDIANTE) no se pueden renombrar ni eliminar. */
@Service
public class RolServicio {

    private final RolRepositorio roles;
    private final UsuarioRepositorio usuarios;
    private final Buscador buscar;

    public RolServicio(RolRepositorio roles, UsuarioRepositorio usuarios, Buscador buscar) {
        this.roles = roles;
        this.usuarios = usuarios;
        this.buscar = buscar;
    }

    @Transactional(readOnly = true)
    public List<RolResponse> listar() {
        List<RolResponse> salida = new ArrayList<>();
        for (Rol r : roles.findAll()) {
            salida.add(Conversor.aRol(r));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public RolResponse obtener(Long id) {
        return Conversor.aRol(buscar.rol(id));
    }

    @Transactional
    public RolResponse crear(RolRequest req) {
        String nombre = req.getNombre().trim().toUpperCase();
        if (roles.existsByNombreIgnoreCase(nombre)) {
            throw new ConflictoException("Ya existe un rol llamado " + nombre);
        }
        Rol rol = new Rol();
        rol.setNombre(nombre);
        rol.setDescripcion(req.getDescripcion());
        return Conversor.aRol(roles.save(rol));
    }

    @Transactional
    public RolResponse actualizar(Long id, RolRequest req) {
        Rol rol = buscar.rol(id);
        String nombre = req.getNombre().trim().toUpperCase();
        if (Roles.esBase(rol.getNombre()) && !rol.getNombre().equalsIgnoreCase(nombre)) {
            throw new ConflictoException("El rol base " + rol.getNombre() + " no se puede renombrar");
        }
        if (!rol.getNombre().equalsIgnoreCase(nombre) && roles.existsByNombreIgnoreCase(nombre)) {
            throw new ConflictoException("Ya existe un rol llamado " + nombre);
        }
        rol.setNombre(nombre);
        rol.setDescripcion(req.getDescripcion());
        return Conversor.aRol(roles.save(rol));
    }

    @Transactional
    public void eliminar(Long id) {
        Rol rol = buscar.rol(id);
        if (Roles.esBase(rol.getNombre())) {
            throw new ConflictoException("El rol base " + rol.getNombre() + " no se puede eliminar");
        }
        if (usuarios.existsByRol_Id(id)) {
            throw new ConflictoException("No se puede eliminar: hay usuarios con este rol");
        }
        roles.delete(rol);
    }
}
