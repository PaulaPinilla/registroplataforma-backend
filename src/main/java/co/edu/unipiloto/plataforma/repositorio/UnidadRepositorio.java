package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.Unidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidadRepositorio extends JpaRepository<Unidad, Long> {

    List<Unidad> findByCurso_IdOrderByOrdenAsc(Long cursoId);
    void deleteByCurso_Id(Long cursoId);
}
