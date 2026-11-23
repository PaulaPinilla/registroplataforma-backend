package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.*;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.EntregaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.PasoProcesoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.ProcesoFormativoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Procesos de formacion: conjunto de actividades de un curso que se deben entregar en secuencia.
 * El estudiante solo puede entregar la actividad N cuando ya entrego la N-1 (se valida en EntregaServicio).
 */
@Service
public class ProcesoServicio {

    private final ProcesoFormativoRepositorio procesos;
    private final PasoProcesoRepositorio pasos;
    private final EntregaRepositorio entregas;
    private final MatriculaRepositorio matriculas;
    private final Permisos permisos;
    private final Buscador buscar;

    public ProcesoServicio(ProcesoFormativoRepositorio procesos, PasoProcesoRepositorio pasos,
                           EntregaRepositorio entregas, MatriculaRepositorio matriculas, Permisos permisos,
                           Buscador buscar) {
        this.procesos = procesos;
        this.pasos = pasos;
        this.entregas = entregas;
        this.matriculas = matriculas;
        this.permisos = permisos;
        this.buscar = buscar;
    }

    @Transactional
    public ProcesoResponse crear(ProcesoRequest req) {
        Curso curso = permisos.exigirDocenteDelCurso(req.getCursoId(), req.getDocenteId(), "crear procesos de formacion");
        ProcesoFormativo p = new ProcesoFormativo();
        p.setCurso(curso);
        p.setNombre(req.getNombre().trim());
        p.setDescripcion(req.getDescripcion());
        List<Actividad> secuencia = validarActividades(curso, req.getActividadIds(), null);
        p = procesos.save(p);
        guardarPasos(p, secuencia);
        return aRespuesta(p);
    }

    @Transactional
    public ProcesoResponse actualizar(Long id, ProcesoRequest req) {
        ProcesoFormativo p = proceso(id);
        Curso curso = permisos.exigirDocenteDelCurso(p.getCurso().getId(), req.getDocenteId(), "modificar procesos de formacion");
        List<Actividad> secuencia = validarActividades(curso, req.getActividadIds(), id);
        p.setNombre(req.getNombre().trim());
        p.setDescripcion(req.getDescripcion());
        p = procesos.save(p);
        pasos.deleteByProceso_Id(id);
        pasos.flush();
        guardarPasos(p, secuencia);
        return aRespuesta(p);
    }

    @Transactional
    public void eliminar(Long id, Long docenteId) {
        ProcesoFormativo p = proceso(id);
        permisos.exigirDocenteDelCurso(p.getCurso().getId(), docenteId, "eliminar procesos de formacion");
        pasos.deleteByProceso_Id(id);
        procesos.delete(p);
    }

    @Transactional(readOnly = true)
    public List<ProcesoResponse> listar(Long cursoId) {
        List<ProcesoFormativo> base = cursoId == null ? procesos.findAll() : procesos.findByCurso_Id(cursoId);
        List<ProcesoResponse> salida = new ArrayList<>();
        for (ProcesoFormativo p : base) {
            salida.add(aRespuesta(p));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public ProcesoResponse obtener(Long id) {
        return aRespuesta(proceso(id));
    }

    /** Estado de cada paso para un estudiante: BLOQUEADO, DISPONIBLE, ENTREGADA o CALIFICADA. */
    @Transactional(readOnly = true)
    public ProcesoProgresoResponse progreso(Long id, Long estudianteId) {
        ProcesoFormativo p = proceso(id);
        Usuario est = buscar.usuario(estudianteId);
        if (!matriculas.existsByEstudiante_IdAndCurso_Id(est.getId(), p.getCurso().getId())) {
            throw new RecursoNoEncontradoException("El estudiante " + estudianteId + " no esta matriculado en el curso de este proceso");
        }
        List<PasoProceso> lista = pasos.findByProceso_IdOrderByOrdenAsc(id);
        Map<Long, Entrega> mapa = new HashMap<>();
        for (PasoProceso paso : lista) {
            Optional<Entrega> e = entregas.findByActividad_IdAndEstudiante_Id(paso.getActividad().getId(), est.getId());
            if (e.isPresent()) {
                mapa.put(paso.getActividad().getId(), e.get());
            }
        }
        List<PasoProgreso> detalle = calcularPasos(lista, mapa);
        int entregados = 0;
        Long siguiente = null;
        for (PasoProgreso pp : detalle) {
            if ("ENTREGADA".equals(pp.getEstado()) || "CALIFICADA".equals(pp.getEstado())) {
                entregados++;
            } else if ("DISPONIBLE".equals(pp.getEstado()) && siguiente == null) {
                siguiente = pp.getActividadId();
            }
        }
        ProcesoProgresoResponse r = new ProcesoProgresoResponse();
        r.setProcesoId(p.getId());
        r.setNombre(p.getNombre());
        r.setEstudianteId(est.getId());
        r.setPasosTotales(detalle.size());
        r.setPasosEntregados(entregados);
        r.setPorcentajeCompletado(detalle.isEmpty() ? 0 : Numeros.redondear(entregados * 100.0 / detalle.size()));
        r.setSiguienteActividadId(siguiente);
        r.setPasos(detalle);
        return r;
    }

    /**
     * Calculo puro: recibe los pasos ordenados y las entregas del estudiante (por id de actividad).
     * Un paso sin entrega esta DISPONIBLE si todos los anteriores ya se entregaron; si no, BLOQUEADO.
     */
    public static List<PasoProgreso> calcularPasos(List<PasoProceso> ordenados, Map<Long, Entrega> entregasPorActividad) {
        List<PasoProgreso> salida = new ArrayList<>();
        boolean bloqueado = false;
        for (PasoProceso paso : ordenados) {
            Entrega e = entregasPorActividad.get(paso.getActividad().getId());
            PasoProgreso pp = new PasoProgreso();
            pp.setOrden(paso.getOrden());
            pp.setActividadId(paso.getActividad().getId());
            pp.setTitulo(paso.getActividad().getTitulo());
            if (e != null) {
                pp.setEstado(e.estaCalificada() ? "CALIFICADA" : "ENTREGADA");
                pp.setNota(e.getCalificacion());
            } else {
                pp.setEstado(bloqueado ? "BLOQUEADO" : "DISPONIBLE");
                bloqueado = true;
            }
            salida.add(pp);
        }
        return salida;
    }

    // ------------------------------------------------------------- internos

    private List<Actividad> validarActividades(Curso curso, List<Long> ids, Long procesoActualId) {
        Set<Long> vistos = new HashSet<>();
        List<Actividad> secuencia = new ArrayList<>();
        for (Long actId : ids) {
            if (!vistos.add(actId)) {
                throw new ReglaNegocioException("La actividad " + actId + " esta repetida en el proceso");
            }
            Actividad a = buscar.actividad(actId);
            if (!a.getUnidad().getCurso().getId().equals(curso.getId())) {
                throw new ReglaNegocioException("La actividad " + actId + " no pertenece al curso " + curso.getId());
            }
            Optional<PasoProceso> existente = pasos.findByActividad_Id(actId);
            if (existente.isPresent() && !existente.get().getProceso().getId().equals(procesoActualId)) {
                throw new ConflictoException("La actividad " + actId + " ya hace parte de otro proceso de formacion");
            }
            secuencia.add(a);
        }
        return secuencia;
    }

    private void guardarPasos(ProcesoFormativo p, List<Actividad> secuencia) {
        int orden = 1;
        for (Actividad a : secuencia) {
            PasoProceso paso = new PasoProceso();
            paso.setProceso(p);
            paso.setActividad(a);
            paso.setOrden(orden++);
            pasos.save(paso);
        }
    }

    private ProcesoResponse aRespuesta(ProcesoFormativo p) {
        ProcesoResponse r = new ProcesoResponse();
        r.setId(p.getId());
        r.setCursoId(p.getCurso().getId());
        r.setNombre(p.getNombre());
        r.setDescripcion(p.getDescripcion());
        List<PasoResponse> lista = new ArrayList<>();
        for (PasoProceso paso : pasos.findByProceso_IdOrderByOrdenAsc(p.getId())) {
            PasoResponse pr = new PasoResponse();
            pr.setOrden(paso.getOrden());
            pr.setActividadId(paso.getActividad().getId());
            pr.setActividadTitulo(paso.getActividad().getTitulo());
            lista.add(pr);
        }
        r.setPasos(lista);
        return r;
    }

    private ProcesoFormativo proceso(Long id) {
        return procesos.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("El proceso de formacion", id));
    }
}
