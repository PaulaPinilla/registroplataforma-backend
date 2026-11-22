package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.CursoRequest;
import co.edu.unipiloto.plataforma.dto.CursoResponse;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.ActividadRepositorio;
import co.edu.unipiloto.plataforma.repositorio.CursoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.UnidadRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de cursos y procedimiento de creacion de cursos:
 * valida docente, pesos de los cortes (suman 100), oferta abierta y fecha de finalizacion.
 */
@Service
public class CursoServicio {

    private final CursoRepositorio cursos;
    private final UnidadRepositorio unidades;
    private final ActividadRepositorio actividades;
    private final Buscador buscar;
    private final Limpieza limpieza;

    public CursoServicio(CursoRepositorio cursos, UnidadRepositorio unidades, ActividadRepositorio actividades,
                         Buscador buscar, Limpieza limpieza) {
        this.cursos = cursos;
        this.unidades = unidades;
        this.actividades = actividades;
        this.buscar = buscar;
        this.limpieza = limpieza;
    }

    @Transactional(readOnly = true)
    public List<CursoResponse> listar(Long docenteId, Long ofertaId) {
        List<Curso> base;
        if (docenteId != null) {
            base = cursos.findByDocente_Id(docenteId);
        } else if (ofertaId != null) {
            base = cursos.findByOferta_Id(ofertaId);
        } else {
            base = cursos.findAll();
        }
        List<CursoResponse> salida = new ArrayList<>();
        for (Curso c : base) {
            salida.add(Conversor.aCurso(c));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public CursoResponse obtener(Long id) {
        return Conversor.aCurso(buscar.curso(id));
    }

    @Transactional
    public CursoResponse crear(CursoRequest req) {
        Curso c = new Curso();
        aplicar(req, c, true);
        return Conversor.aCurso(cursos.save(c));
    }

    @Transactional
    public CursoResponse actualizar(Long id, CursoRequest req) {
        Curso c = buscar.curso(id);
        aplicar(req, c, false);
        return Conversor.aCurso(cursos.save(c));
    }

    @Transactional
    public void eliminar(Long id) {
        Curso c = buscar.curso(id);
        limpieza.borrarCurso(id);
        cursos.delete(c);
    }

    /** Procedimiento: copia un curso (con sus unidades y actividades) para un nuevo semestre. */
    @Transactional
    public CursoResponse duplicar(Long id, String semestreNuevo, LocalDateTime nuevaFinalizacion) {
        Curso origen = buscar.curso(id);
        if (semestreNuevo == null || semestreNuevo.trim().isEmpty()) {
            throw new ReglaNegocioException("El semestre del nuevo curso es obligatorio");
        }
        if (nuevaFinalizacion == null || !nuevaFinalizacion.isAfter(LocalDateTime.now())) {
            throw new ReglaNegocioException("La nueva fecha de finalizacion debe ser futura");
        }
        Curso copia = new Curso();
        copia.setTitulo(origen.getTitulo());
        copia.setDescripcion(origen.getDescripcion());
        copia.setSemestre(semestreNuevo.trim());
        copia.setFechaFinalizacion(nuevaFinalizacion);
        copia.setPesoCorte1(origen.getPesoCorte1());
        copia.setPesoCorte2(origen.getPesoCorte2());
        copia.setPesoCorte3(origen.getPesoCorte3());
        copia.setClaveMatricula(origen.getClaveMatricula());
        copia.setDocente(origen.getDocente());
        copia.setOferta(origen.getOferta());
        copia = cursos.save(copia);

        for (Unidad u : unidades.findByCurso_IdOrderByOrdenAsc(origen.getId())) {
            Unidad nueva = new Unidad();
            nueva.setTitulo(u.getTitulo());
            nueva.setOrden(u.getOrden());
            nueva.setCurso(copia);
            nueva = unidades.save(nueva);
            for (Actividad a : actividades.findByUnidad_Id(u.getId())) {
                Actividad na = new Actividad();
                na.setTitulo(a.getTitulo());
                na.setInstrucciones(a.getInstrucciones());
                na.setFechaInicio(a.getFechaInicio());
                na.setFechaLimite(a.getFechaLimite());
                na.setTipoEntrega(a.getTipoEntrega());
                na.setCategoria(a.getCategoria());
                na.setCorte(a.getCorte());
                na.setPorcentaje(a.getPorcentaje());
                na.setFechaCreacion(LocalDateTime.now());
                na.setUnidad(nueva);
                actividades.save(na);
            }
        }
        return Conversor.aCurso(copia);
    }

    private void aplicar(CursoRequest req, Curso c, boolean esNuevo) {
        Usuario docente = buscar.usuario(req.getDocenteId());
        if (!Roles.DOCENTE.equalsIgnoreCase(docente.getRol().getNombre())) {
            throw new ReglaNegocioException("El usuario " + docente.getId() + " no tiene rol DOCENTE");
        }
        if (!docente.isActivo()) {
            throw new ReglaNegocioException("El docente esta desactivado");
        }
        double suma = req.getPesoCorte1() + req.getPesoCorte2() + req.getPesoCorte3();
        if (Math.abs(suma - 100.0) > 0.001) {
            throw new ReglaNegocioException("Los pesos de los tres cortes deben sumar 100 (suman " + suma + ")");
        }
        if (esNuevo && !req.getFechaFinalizacion().isAfter(LocalDateTime.now())) {
            throw new ReglaNegocioException("La fecha de finalizacion debe ser futura");
        }
        OfertaEducativa oferta = null;
        if (req.getOfertaId() != null) {
            oferta = buscar.oferta(req.getOfertaId());
            boolean cambiaOferta = esNuevo || c.getOferta() == null || !c.getOferta().getId().equals(oferta.getId());
            if (cambiaOferta && oferta.getEstado() == EstadoOferta.CERRADA) {
                throw new ReglaNegocioException("La oferta " + oferta.getId() + " esta CERRADA");
            }
        }
        c.setTitulo(req.getTitulo().trim());
        c.setDescripcion(req.getDescripcion());
        c.setSemestre(req.getSemestre().trim());
        c.setFechaFinalizacion(req.getFechaFinalizacion());
        c.setPesoCorte1(req.getPesoCorte1());
        c.setPesoCorte2(req.getPesoCorte2());
        c.setPesoCorte3(req.getPesoCorte3());
        String clave = req.getClaveMatricula();
        c.setClaveMatricula(clave == null || clave.trim().isEmpty() ? null : clave.trim());
        c.setDocente(docente);
        c.setOferta(oferta);
    }
}
