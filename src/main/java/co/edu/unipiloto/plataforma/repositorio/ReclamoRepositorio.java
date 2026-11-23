package co.edu.unipiloto.plataforma.repositorio;

import co.edu.unipiloto.plataforma.modelo.EstadoReclamo;
import co.edu.unipiloto.plataforma.modelo.Reclamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamoRepositorio extends JpaRepository<Reclamo, Long> {

    List<Reclamo> findByEntrega_Id(Long entregaId);
    List<Reclamo> findByEstudiante_Id(Long estudianteId);
    List<Reclamo> findByEntrega_Actividad_Unidad_Curso_Id(Long cursoId);
    List<Reclamo> findByEntrega_Actividad_Unidad_Curso_Docente_Id(Long docenteId);
    long countByEstado(EstadoReclamo estado);
    void deleteByEntrega_Id(Long entregaId);
    void deleteByEntrega_Actividad_Id(Long actividadId);
}
