package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.Curso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepositorio extends JpaRepository<Curso, Long> {

    List<Curso> findByDocente_Id(Long docenteId);
    List<Curso> findByOferta_Id(Long ofertaId);
    boolean existsByOferta_Id(Long ofertaId);
    boolean existsByDocente_Id(Long docenteId);
}
