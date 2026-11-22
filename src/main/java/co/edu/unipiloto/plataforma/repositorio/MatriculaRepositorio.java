package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.Matricula;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepositorio extends JpaRepository<Matricula, Long> {

    List<Matricula> findByEstudiante_Id(Long estudianteId);
    List<Matricula> findByCurso_Id(Long cursoId);
    Optional<Matricula> findByEstudiante_IdAndCurso_Id(Long estudianteId, Long cursoId);
    boolean existsByEstudiante_IdAndCurso_Id(Long estudianteId, Long cursoId);
    void deleteByCurso_Id(Long cursoId);
    boolean existsByEstudiante_Id(Long estudianteId);
}
