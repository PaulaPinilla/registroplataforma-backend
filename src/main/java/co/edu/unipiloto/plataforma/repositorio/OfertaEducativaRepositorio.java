package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.EstadoOferta;
import co.edu.unipiloto.plataforma.modelo.OfertaEducativa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfertaEducativaRepositorio extends JpaRepository<OfertaEducativa, Long> {

    List<OfertaEducativa> findByEstado(EstadoOferta estado);
    boolean existsByNombreIgnoreCase(String nombre);
}
