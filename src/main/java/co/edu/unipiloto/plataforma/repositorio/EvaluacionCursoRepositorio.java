package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.EvaluacionCurso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluacionCursoRepositorio extends JpaRepository<EvaluacionCurso, Long> {

    List<EvaluacionCurso> findByCurso_Id(Long cursoId);
    Optional<EvaluacionCurso> findByCurso_IdAndEstudiante_Id(Long cursoId, Long estudianteId);
    void deleteByCurso_Id(Long cursoId);
    boolean existsByEstudiante_Id(Long estudianteId);
}
