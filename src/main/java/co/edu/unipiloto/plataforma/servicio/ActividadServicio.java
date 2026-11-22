package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.ActividadRequest;
import co.edu.unipiloto.plataforma.dto.ActividadResponse;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.Actividad;
import co.edu.unipiloto.plataforma.modelo.Unidad;
import co.edu.unipiloto.plataforma.repositorio.ActividadRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de actividades del curso.
 * Regla: dentro de un mismo corte los porcentajes de las actividades no pueden superar 100.
 */
@Service
public class ActividadServicio {

    private final ActividadRepositorio actividades;
    private final Buscador buscar;
    private final Limpieza limpieza;

    public ActividadServicio(ActividadRepositorio actividades, Buscador buscar, Limpieza limpieza) {
        this.actividades = actividades;
        this.buscar = buscar;
        this.limpieza = limpieza;
    }

    @Transactional(readOnly = true)
    public List<ActividadResponse> listar(Long unidadId, Long cursoId) {
        List<Actividad> base;
        if (unidadId != null) {
            buscar.unidad(unidadId);
            base = actividades.findByUnidad_Id(unidadId);
        } else if (cursoId != null) {
            buscar.curso(cursoId);
            base = actividades.findByUnidad_Curso_Id(cursoId);
        } else {
            base = actividades.findAll();
        }
        List<ActividadResponse> salida = new ArrayList<>();
        for (Actividad a : base) {
            salida.add(Conversor.aActividad(a));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public ActividadResponse obtener(Long id) {
        return Conversor.aActividad(buscar.actividad(id));
    }

    @Transactional
    public ActividadResponse crear(ActividadRequest req) {
        Actividad a = new Actividad();
        a.setFechaCreacion(LocalDateTime.now());
        aplicar(req, a);
        return Conversor.aActividad(actividades.save(a));
    }

    @Transactional
    public ActividadResponse actualizar(Long id, ActividadRequest req) {
        Actividad a = buscar.actividad(id);
        aplicar(req, a);
        return Conversor.aActividad(actividades.save(a));
    }

    @Transactional
    public void eliminar(Long id) {
        buscar.actividad(id);
        limpieza.borrarActividad(id);
    }

    private void aplicar(ActividadRequest req, Actividad a) {
        if (!req.getFechaLimite().isAfter(req.getFechaInicio())) {
            throw new ReglaNegocioException("La fecha limite debe ser posterior a la fecha de inicio");
        }
        Unidad unidad = buscar.unidad(req.getUnidadId());
        Long cursoId = unidad.getCurso().getId();

        double usado = 0;
        for (Actividad otra : actividades.findByUnidad_Curso_Id(cursoId)) {
            boolean esLaMisma = a.getId() != null && a.getId().equals(otra.getId());
            if (!esLaMisma && otra.getCorte() == req.getCorte()) {
                usado += otra.getPorcentaje();
            }
        }
        if (usado + req.getPorcentaje() > 100.0 + 0.001) {
            throw new ReglaNegocioException("En el corte " + req.getCorte() + " ya hay " + usado
                    + "% asignado; con este porcentaje (" + req.getPorcentaje() + "%) se pasa de 100%");
        }
        a.setTitulo(req.getTitulo().trim());
        a.setInstrucciones(req.getInstrucciones());
        a.setFechaInicio(req.getFechaInicio());
        a.setFechaLimite(req.getFechaLimite());
        a.setTipoEntrega(req.getTipoEntrega());
        a.setCategoria(req.getCategoria());
        a.setCorte(req.getCorte());
        a.setPorcentaje(req.getPorcentaje());
        a.setUnidad(unidad);
    }
}
