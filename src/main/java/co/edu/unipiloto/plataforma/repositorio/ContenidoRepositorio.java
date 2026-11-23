package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.Contenido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContenidoRepositorio extends JpaRepository<Contenido, Long> {

    List<Contenido> findByUnidad_Id(Long unidadId);
    List<Contenido> findByUnidad_Curso_Id(Long cursoId);
    void deleteByUnidad_Id(Long unidadId);
}
