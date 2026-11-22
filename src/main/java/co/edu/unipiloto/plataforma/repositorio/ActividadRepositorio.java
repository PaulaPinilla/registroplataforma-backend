package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.Actividad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadRepositorio extends JpaRepository<Actividad, Long> {

    List<Actividad> findByUnidad_Id(Long unidadId);
    List<Actividad> findByUnidad_Curso_Id(Long cursoId);
    void deleteByUnidad_Id(Long unidadId);
}
