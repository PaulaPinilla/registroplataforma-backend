package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.*;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cursos (modulos).
 *  - Docente: crea y configura el curso.
 *  - Coordinador: asigna el docente, fija objetivos, resultados, cupos y fechas, y publica el curso;
 *    al hacerlo se notifica al docente y a los estudiantes.
 */
@Service
public class CursoServicio {

    private final CursoRepositorio cursos;
    private final UnidadRepositorio unidades;
    private final ActividadRepositorio actividades;
    private final ContenidoRepositorio contenidos;
    private final MatriculaRepositorio matriculas;
    private final Buscador buscar;
    private final Limpieza limpieza;
    private final Permisos permisos;
    private final NotificacionServicio notificaciones;

    public CursoServicio(CursoRepositorio cursos, UnidadRepositorio unidades, ActividadRepositorio actividades,
                         ContenidoRepositorio contenidos, MatriculaRepositorio matriculas, Buscador buscar,
                         Limpieza limpieza, Permisos permisos, NotificacionServicio notificaciones) {
        this.cursos = cursos;
        this.unidades = unidades;
        this.actividades = actividades;
        this.contenidos = contenidos;
        this.matriculas = matriculas;
        this.buscar = buscar;
        this.limpieza = limpieza;
        this.permisos = permisos;
        this.notificaciones = notificaciones;
    }

    // ------------------------------------------------------------------ CRUD

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
        c.setPublicado(false);
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

    /** Procedimiento: copia un curso (con sus unidades y actividades) para un nuevo semestre. Queda sin publicar. */
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
        copia.setObjetivos(origen.getObjetivos());
        copia.setResultadosAprendizaje(origen.getResultadosAprendizaje());
        copia.setCupos(origen.getCupos());
        copia.setPublicado(false);
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

    // ------------------------------------------------ Coordinador: publicacion

    /** El coordinador fija objetivos, resultados, cupos y fechas, y publica el curso. */
    @Transactional
    public CursoResponse publicar(Long id, PublicacionCursoRequest req) {
        Usuario coordinador = permisos.exigirRol(req.getCoordinadorId(), Roles.COORDINADOR, "publicar cursos");
        Curso curso = buscar.curso(id);
        if (!curso.tienePesosValidos()) {
            throw new ReglaNegocioException("No se puede publicar: los pesos de los cortes deben sumar 100");
        }
        if (!req.getFechaFinalizacion().isAfter(req.getFechaInicio())) {
            throw new ReglaNegocioException("La fecha de finalizacion debe ser posterior a la fecha de inicio");
        }
        if (!req.getFechaFinalizacion().isAfter(LocalDateTime.now())) {
            throw new ReglaNegocioException("La fecha de finalizacion debe ser futura");
        }
        if (unidades.findByCurso_IdOrderByOrdenAsc(id).isEmpty()) {
            throw new ReglaNegocioException("No se puede publicar un curso sin unidades tematicas");
        }
        if (actividades.findByUnidad_Curso_Id(id).isEmpty()) {
            throw new ReglaNegocioException("No se puede publicar un curso sin actividades");
        }
        int yaMatriculados = matriculas.findByCurso_Id(id).size();
        if (req.getCupos() < yaMatriculados) {
            throw new ReglaNegocioException("Los cupos (" + req.getCupos() + ") no pueden ser menores a los estudiantes ya matriculados ("
                    + yaMatriculados + ")");
        }
        boolean eraPublicado = curso.estaPublicado();
        curso.setObjetivos(req.getObjetivos().trim());
        curso.setResultadosAprendizaje(req.getResultadosAprendizaje().trim());
        curso.setCupos(req.getCupos());
        curso.setFechaInicio(req.getFechaInicio());
        curso.setFechaFinalizacion(req.getFechaFinalizacion());
        curso.setPublicado(true);
        curso = cursos.save(curso);

        if (!eraPublicado) {
            String texto = "Curso disponible: " + curso.getTitulo() + ". Inicia el " + curso.getFechaInicio()
                    + ", cupos: " + curso.getCupos() + ". Objetivos: " + curso.getObjetivos();
            notificaciones.crear(curso.getDocente(), coordinador, TipoNotificacion.CURSO_PUBLICADO,
                    "Su curso fue publicado: " + curso.getTitulo(), texto, curso.getId());
            notificaciones.notificarRol(Roles.ESTUDIANTE, coordinador, TipoNotificacion.CURSO_PUBLICADO,
                    "Nuevo curso disponible: " + curso.getTitulo(), texto, curso.getId());
        }
        return Conversor.aCurso(curso);
    }

    @Transactional
    public CursoResponse despublicar(Long id, Long coordinadorId) {
        permisos.exigirRol(coordinadorId, Roles.COORDINADOR, "despublicar cursos");
        Curso curso = buscar.curso(id);
        if (!matriculas.findByCurso_Id(id).isEmpty()) {
            throw new ConflictoException("No se puede despublicar: el curso ya tiene estudiantes matriculados");
        }
        curso.setPublicado(false);
        return Conversor.aCurso(cursos.save(curso));
    }

    /** El coordinador asigna (o cambia) el docente de un curso y se le notifica la asignacion. */
    @Transactional
    public CursoResponse asignarDocente(Long id, AsignacionDocenteRequest req) {
        Usuario coordinador = permisos.exigirRol(req.getCoordinadorId(), Roles.COORDINADOR, "asignar docentes");
        Curso curso = buscar.curso(id);
        Usuario docente = permisos.exigirRol(req.getDocenteId(), Roles.DOCENTE, "ser asignado como docente de un curso");
        if (curso.getDocente().getId().equals(docente.getId())) {
            throw new ConflictoException("Ese docente ya esta asignado a este curso");
        }
        curso.setDocente(docente);
        curso = cursos.save(curso);
        notificaciones.crear(docente, coordinador, TipoNotificacion.ASIGNACION_CURSO,
                "Se le asigno el curso " + curso.getTitulo(),
                "El coordinador " + coordinador.getNombre() + " le asigno el curso '" + curso.getTitulo()
                        + "' del semestre " + curso.getSemestre() + ".", curso.getId());
        return Conversor.aCurso(curso);
    }

    /** Cursos publicados y vigentes con sus objetivos, resultados, cupos y fechas. */
    @Transactional(readOnly = true)
    public List<CursoCatalogoResponse> catalogo() {
        List<CursoCatalogoResponse> salida = new ArrayList<>();
        for (Curso c : cursos.findAll()) {
            if (!c.estaPublicado() || c.yaTermino()) {
                continue;
            }
            long matriculados = matriculas.findByCurso_Id(c.getId()).size();
            CursoCatalogoResponse d = new CursoCatalogoResponse();
            d.setId(c.getId());
            d.setTitulo(c.getTitulo());
            d.setDescripcion(c.getDescripcion());
            d.setObjetivos(c.getObjetivos());
            d.setResultadosAprendizaje(c.getResultadosAprendizaje());
            d.setDocenteNombre(c.getDocente().getNombre());
            d.setFechaInicio(c.getFechaInicio());
            d.setFechaFinalizacion(c.getFechaFinalizacion());
            d.setCupos(c.getCupos());
            d.setMatriculados(matriculados);
            d.setCuposDisponibles(c.getCupos() == null ? 0 : Math.max(0, c.getCupos() - matriculados));
            d.setExigeClave(c.exigeClave());
            salida.add(d);
        }
        return salida;
    }

    // ------------------------------------------------ Docente: configuracion

    /** Resumen de que le falta a un curso para estar completo (secciones, porcentajes, contenidos). */
    @Transactional(readOnly = true)
    public ConfiguracionCursoResponse configuracion(Long id) {
        Curso curso = buscar.curso(id);
        List<Unidad> unids = unidades.findByCurso_IdOrderByOrdenAsc(id);
        List<Actividad> acts = actividades.findByUnidad_Curso_Id(id);
        int totalContenidos = contenidos.findByUnidad_Curso_Id(id).size();

        List<String> pendientes = new ArrayList<>();
        if (!curso.tienePesosValidos()) {
            pendientes.add("Los pesos de los tres cortes no suman 100");
        }
        if (unids.isEmpty()) {
            pendientes.add("El curso no tiene unidades tematicas");
        }
        if (acts.isEmpty()) {
            pendientes.add("El curso no tiene actividades");
        }
        if (totalContenidos == 0) {
            pendientes.add("El curso no tiene contenidos publicados");
        }
        List<CortePorcentaje> cortes = new ArrayList<>();
        for (int corte = 1; corte <= 3; corte++) {
            int cantidad = 0;
            double suma = 0;
            for (Actividad a : acts) {
                if (a.getCorte() == corte) {
                    cantidad++;
                    suma += a.getPorcentaje();
                }
            }
            double peso = curso.pesoDelCorte(corte);
            boolean completo = Math.abs(suma - 100.0) < 0.01;
            if (peso > 0 && cantidad == 0) {
                pendientes.add("El corte " + corte + " (peso " + peso + "%) no tiene actividades");
            } else if (peso > 0 && !completo) {
                pendientes.add("Las actividades del corte " + corte + " suman " + Numeros.redondear(suma) + "% (deben sumar 100%)");
            }
            CortePorcentaje cp = new CortePorcentaje();
            cp.setCorte(corte);
            cp.setPesoCorte(peso);
            cp.setActividades(cantidad);
            cp.setPorcentajeAsignado(Numeros.redondear(suma));
            cp.setCompleto(completo);
            cortes.add(cp);
        }
        ConfiguracionCursoResponse r = new ConfiguracionCursoResponse();
        r.setCursoId(curso.getId());
        r.setTitulo(curso.getTitulo());
        r.setPesosValidos(curso.tienePesosValidos());
        r.setTotalUnidades(unids.size());
        r.setTotalActividades(acts.size());
        r.setTotalContenidos(totalContenidos);
        r.setCortes(cortes);
        r.setListoParaPublicar(pendientes.isEmpty());
        r.setPublicado(curso.estaPublicado());
        r.setPendientes(pendientes);
        return r;
    }

    // ------------------------------------------------------------- internos

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
        if (req.getFechaInicio() != null && !req.getFechaFinalizacion().isAfter(req.getFechaInicio())) {
            throw new ReglaNegocioException("La fecha de finalizacion debe ser posterior a la fecha de inicio");
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
        // campos opcionales: si no vienen, se conserva lo que ya tenia el curso
        if (req.getObjetivos() != null) {
            c.setObjetivos(req.getObjetivos());
        }
        if (req.getResultadosAprendizaje() != null) {
            c.setResultadosAprendizaje(req.getResultadosAprendizaje());
        }
        if (req.getCupos() != null) {
            c.setCupos(req.getCupos());
        }
        if (req.getFechaInicio() != null) {
            c.setFechaInicio(req.getFechaInicio());
        }
    }
}
