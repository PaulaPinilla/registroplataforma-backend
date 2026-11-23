package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.Notificacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByDestinatario_IdOrderByIdDesc(Long usuarioId);
    List<Notificacion> findByDestinatario_IdAndLeidaOrderByIdDesc(Long usuarioId, Boolean leida);
}
