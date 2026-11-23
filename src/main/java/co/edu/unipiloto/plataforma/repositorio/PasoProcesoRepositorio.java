package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.PasoProceso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasoProcesoRepositorio extends JpaRepository<PasoProceso, Long> {

    List<PasoProceso> findByProceso_IdOrderByOrdenAsc(Long procesoId);
    Optional<PasoProceso> findByActividad_Id(Long actividadId);
    boolean existsByActividad_Id(Long actividadId);
    void deleteByProceso_Id(Long procesoId);
    void deleteByActividad_Id(Long actividadId);
}
