package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.Entrega;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntregaRepositorio extends JpaRepository<Entrega, Long> {

    List<Entrega> findByActividad_Id(Long actividadId);
    List<Entrega> findByEstudiante_Id(Long estudianteId);
    Optional<Entrega> findByActividad_IdAndEstudiante_Id(Long actividadId, Long estudianteId);
    List<Entrega> findByActividad_Unidad_Curso_Id(Long cursoId);
    List<Entrega> findByActividad_Unidad_Curso_IdAndEstudiante_Id(Long cursoId, Long estudianteId);
    void deleteByActividad_Id(Long actividadId);
    boolean existsByEstudiante_Id(Long estudianteId);
}
