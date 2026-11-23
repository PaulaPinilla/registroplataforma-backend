package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.MensajeForo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajeForoRepositorio extends JpaRepository<MensajeForo, Long> {

    List<MensajeForo> findByForo_IdOrderByIdAsc(Long foroId);
    List<MensajeForo> findByForo_IdAndIdGreaterThanOrderByIdAsc(Long foroId, Long id);
    long countByForo_Id(Long foroId);
    void deleteByForo_Id(Long foroId);
}
