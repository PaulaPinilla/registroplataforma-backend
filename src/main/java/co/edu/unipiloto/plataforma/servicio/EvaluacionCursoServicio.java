package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.EvaluacionCursoActualizarRequest;
import co.edu.unipiloto.plataforma.dto.EvaluacionCursoRequest;
import co.edu.unipiloto.plataforma.dto.EvaluacionCursoResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.modelo.Curso;
import co.edu.unipiloto.plataforma.modelo.EvaluacionCurso;
import co.edu.unipiloto.plataforma.modelo.Usuario;
import co.edu.unipiloto.plataforma.repositorio.EvaluacionCursoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** CRUD de la evaluacion del curso (puntaje 1 a 5 y comentario) hecha por un estudiante matriculado. */
@Service
public class EvaluacionCursoServicio {

    private final EvaluacionCursoRepositorio evaluaciones;
    private final MatriculaRepositorio matriculas;
    private final Buscador buscar;

    public EvaluacionCursoServicio(EvaluacionCursoRepositorio evaluaciones, MatriculaRepositorio matriculas,
                                   Buscador buscar) {
        this.evaluaciones = evaluaciones;
        this.matriculas = matriculas;
        this.buscar = buscar;
    }

    @Transactional(readOnly = true)
    public List<EvaluacionCursoResponse> listar(Long cursoId) {
        List<EvaluacionCurso> base;
        if (cursoId != null) {
            buscar.curso(cursoId);
            base = evaluaciones.findByCurso_Id(cursoId);
        } else {
            base = evaluaciones.findAll();
        }
        List<EvaluacionCursoResponse> salida = new ArrayList<>();
        for (EvaluacionCurso e : base) {
            salida.add(Conversor.aEvaluacion(e));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public EvaluacionCursoResponse obtener(Long id) {
        return Conversor.aEvaluacion(buscar.evaluacion(id));
    }

    @Transactional
    public EvaluacionCursoResponse crear(EvaluacionCursoRequest req) {
        Curso curso = buscar.curso(req.getCursoId());
        Usuario est = buscar.usuario(req.getEstudianteId());
        if (!matriculas.existsByEstudiante_IdAndCurso_Id(est.getId(), curso.getId())) {
            throw new ProhibidoException("Solo un estudiante matriculado puede evaluar el curso");
        }
        if (evaluaciones.findByCurso_IdAndEstudiante_Id(curso.getId(), est.getId()).isPresent()) {
            throw new ConflictoException("El estudiante ya evaluo este curso. Use PUT para modificar su evaluacion");
        }
        EvaluacionCurso e = new EvaluacionCurso();
        e.setCurso(curso);
        e.setEstudiante(est);
        e.setPuntaje(req.getPuntaje());
        e.setComentario(req.getComentario());
        e.setFecha(LocalDateTime.now());
        return Conversor.aEvaluacion(evaluaciones.save(e));
    }

    @Transactional
    public EvaluacionCursoResponse actualizar(Long id, EvaluacionCursoActualizarRequest req) {
        EvaluacionCurso e = buscar.evaluacion(id);
        e.setPuntaje(req.getPuntaje());
        e.setComentario(req.getComentario());
        e.setFecha(LocalDateTime.now());
        return Conversor.aEvaluacion(evaluaciones.save(e));
    }

    @Transactional
    public void eliminar(Long id) {
        evaluaciones.delete(buscar.evaluacion(id));
    }
}
