package co.edu.unipiloto.plataforma.config;

import co.edu.unipiloto.plataforma.modelo.Rol;
import co.edu.unipiloto.plataforma.modelo.Roles;
import co.edu.unipiloto.plataforma.repositorio.RolRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Al iniciar el servidor crea los tres roles base si todavia no existen. */
@Component
public class DatosIniciales implements CommandLineRunner {

    private final RolRepositorio roles;

    public DatosIniciales(RolRepositorio roles) {
        this.roles = roles;
    }

    @Override
    public void run(String... args) {
        crearSiNoExiste(Roles.COORDINADOR, "Coordinador o director: publica cursos, fija cupos y fechas, asigna docentes, notifica y consulta reportes");
        crearSiNoExiste(Roles.DOCENTE, "Docente: crea cursos y actividades, evalua y retroalimenta");
        crearSiNoExiste(Roles.ESTUDIANTE, "Estudiante: se matricula, entrega actividades y evalua los cursos");
    }

    private void crearSiNoExiste(String nombre, String descripcion) {
        if (!roles.existsByNombreIgnoreCase(nombre)) {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rol.setDescripcion(descripcion);
            roles.save(rol);
        }
    }
}
