package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.modelo.Actividad;
import co.edu.unipiloto.plataforma.modelo.Unidad;
import co.edu.unipiloto.plataforma.repositorio.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Borrado en cascada controlado: elimina primero lo que depende de un curso, unidad o actividad.
 * Se hace aqui (y no con CascadeType.REMOVE) para que el orden sea explicito y facil de explicar.
 */
@Component
public class Limpieza {

    private final EntregaRepositorio entregas;
    private final ActividadRepositorio actividades;
    private final UnidadRepositorio unidades;
    private final MatriculaRepositorio matriculas;
    private final EvaluacionCursoRepositorio evaluaciones;

    public Limpieza(EntregaRepositorio entregas, ActividadRepositorio actividades, UnidadRepositorio unidades,
                    MatriculaRepositorio matriculas, EvaluacionCursoRepositorio evaluaciones) {
        this.entregas = entregas;
        this.actividades = actividades;
        this.unidades = unidades;
        this.matriculas = matriculas;
        this.evaluaciones = evaluaciones;
    }

    @Transactional
    public void borrarActividad(Long actividadId) {
        entregas.deleteByActividad_Id(actividadId);
        actividades.deleteById(actividadId);
    }

    @Transactional
    public void borrarUnidad(Long unidadId) {
        for (Actividad a : actividades.findByUnidad_Id(unidadId)) {
            entregas.deleteByActividad_Id(a.getId());
        }
        actividades.deleteByUnidad_Id(unidadId);
        unidades.deleteById(unidadId);
    }

    @Transactional
    public void borrarCurso(Long cursoId) {
        for (Unidad u : unidades.findByCurso_IdOrderByOrdenAsc(cursoId)) {
            for (Actividad a : actividades.findByUnidad_Id(u.getId())) {
                entregas.deleteByActividad_Id(a.getId());
            }
            actividades.deleteByUnidad_Id(u.getId());
        }
        unidades.deleteByCurso_Id(cursoId);
        matriculas.deleteByCurso_Id(cursoId);
        evaluaciones.deleteByCurso_Id(cursoId);
    }
}
