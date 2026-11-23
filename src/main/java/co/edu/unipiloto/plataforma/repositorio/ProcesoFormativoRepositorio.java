package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.ProcesoFormativo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcesoFormativoRepositorio extends JpaRepository<ProcesoFormativo, Long> {

    List<ProcesoFormativo> findByCurso_Id(Long cursoId);
    void deleteByCurso_Id(Long cursoId);
}
