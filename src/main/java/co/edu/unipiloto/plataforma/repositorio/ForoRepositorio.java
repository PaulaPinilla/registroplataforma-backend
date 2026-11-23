package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.Foro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForoRepositorio extends JpaRepository<Foro, Long> {

    List<Foro> findByCurso_Id(Long cursoId);
    void deleteByCurso_Id(Long cursoId);
}
