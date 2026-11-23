package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.Certificado;
import co.edu.unipiloto.plataforma.modelo.EstadoCertificado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificadoRepositorio extends JpaRepository<Certificado, Long> {

    List<Certificado> findByEstudiante_Id(Long estudianteId);
    List<Certificado> findByCurso_Id(Long cursoId);
    Optional<Certificado> findByEstudiante_IdAndCurso_Id(Long estudianteId, Long cursoId);
    Optional<Certificado> findByCodigo(String codigo);
    long countByEstado(EstadoCertificado estado);
    void deleteByCurso_Id(Long cursoId);
}
