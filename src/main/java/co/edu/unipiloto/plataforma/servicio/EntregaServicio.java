package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.CalificacionRequest;
import co.edu.unipiloto.plataforma.dto.EntregaActualizarRequest;
import co.edu.unipiloto.plataforma.dto.EntregaRequest;
import co.edu.unipiloto.plataforma.dto.EntregaResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.dto.PasoProgreso;
import co.edu.unipiloto.plataforma.repositorio.EntregaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.PasoProcesoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Procesos formativos y de evaluacion:
 *  - el estudiante entrega una actividad (con texto o con archivo), dentro de las fechas, estando matriculado
 *    y respetando el orden si la actividad pertenece a un proceso formativo;
 *  - el docente del curso califica y deja retroalimentacion; el estudiante recibe una notificacion.
 */
@Service
public class EntregaServicio {

    private final EntregaRepositorio entregas;
    private final MatriculaRepositorio matriculas;
    private final PasoProcesoRepositorio pasos;
    private final ArchivoServicio archivos;
    private final NotificacionServicio notificaciones;
    private final Buscador buscar;

    public EntregaServicio(EntregaRepositorio entregas, MatriculaRepositorio matriculas, PasoProcesoRepositorio pasos,
                           ArchivoServicio archivos, NotificacionServicio notificaciones, Buscador buscar) {
        this.entregas = entregas;
        this.matriculas = matriculas;
        this.pasos = pasos;
        this.archivos = archivos;
        this.notificaciones = notificaciones;
        this.buscar = buscar;
    }

    @Transactional(readOnly = true)
    public List<EntregaResponse> listar(Long actividadId, Long estudianteId, Long cursoId) {
        List<Entrega> base;
        if (actividadId != null && estudianteId != null) {
            base = new ArrayList<>();
            entregas.findByActividad_IdAndEstudiante_Id(actividadId, estudianteId).ifPresent(base::add);
        } else if (actividadId != null) {
            base = entregas.findByActividad_Id(actividadId);
        } else if (cursoId != null && estudianteId != null) {
            base = entregas.findByActividad_Unidad_Curso_IdAndEstudiante_Id(cursoId, estudianteId);
        } else if (cursoId != null) {
            base = entregas.findByActividad_Unidad_Curso_Id(cursoId);
        } else if (estudianteId != null) {
            base = entregas.findByEstudiante_Id(estudianteId);
        } else {
            base = entregas.findAll();
        }
        List<EntregaResponse> salida = new ArrayList<>();
        for (Entrega e : base) {
            salida.add(Conversor.aEntrega(e));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public EntregaResponse obtener(Long id) {
        return Conversor.aEntrega(buscar.entrega(id));
    }

    /** Entrega con texto (y/o el nombre del archivo, sin subirlo). */
    @Transactional
    public EntregaResponse entregar(EntregaRequest req) {
        return Conversor.aEntrega(registrar(req));
    }

    /** Entrega subiendo un archivo real (multipart). */
    @Transactional
    public EntregaResponse entregarConArchivo(Long actividadId, Long estudianteId, String texto, MultipartFile archivo) {
        EntregaRequest req = new EntregaRequest();
        req.setActividadId(actividadId);
        req.setEstudianteId(estudianteId);
        req.setTextoRespuesta(texto);
        req.setNombreArchivo(archivos.nombreSeguro(archivo == null ? null : archivo.getOriginalFilename()));
        Entrega e = registrar(req);
        e.setRutaArchivo(archivos.guardar("entregas", archivo));
        return Conversor.aEntrega(entregas.save(e));
    }

    /** El estudiante cambia su archivo antes de que sea calificado. */
    @Transactional
    public EntregaResponse reemplazarArchivo(Long id, Long estudianteId, MultipartFile archivo) {
        Entrega e = buscar.entrega(id);
        if (!e.getEstudiante().getId().equals(estudianteId)) {
            throw new ProhibidoException("Solo el estudiante dueno de la entrega puede cambiar su archivo");
        }
        if (e.estaCalificada()) {
            throw new ConflictoException("La entrega ya fue calificada y no se puede modificar");
        }
        verificarVentana(e.getActividad());
        String nombre = archivos.nombreSeguro(archivo == null ? null : archivo.getOriginalFilename());
        String nueva = archivos.guardar("entregas", archivo);
        archivos.eliminar(e.getRutaArchivo());
        e.setRutaArchivo(nueva);
        e.setNombreArchivo(nombre);
        e.setFechaEntrega(LocalDateTime.now());
        return Conversor.aEntrega(entregas.save(e));
    }

    /** Descarga: el estudiante dueno, el docente del curso o un coordinador. */
    @Transactional(readOnly = true)
    public ArchivoDescargado descargarArchivo(Long id, Long usuarioId) {
        Entrega e = buscar.entrega(id);
        Usuario u = buscar.usuario(usuarioId);
        Curso curso = e.getActividad().getUnidad().getCurso();
        boolean esDueno = e.getEstudiante().getId().equals(u.getId());
        boolean esDocente = curso.getDocente().getId().equals(u.getId());
        boolean esCoordinador = Roles.COORDINADOR.equalsIgnoreCase(u.getRol().getNombre());
        if (!esDueno && !esDocente && !esCoordinador) {
            throw new ProhibidoException("No tiene permiso para descargar esta entrega");
        }
        if (e.getRutaArchivo() == null) {
            throw new RecursoNoEncontradoException("La entrega no tiene archivo adjunto");
        }
        String nombre = e.getNombreArchivo() == null ? "entrega" : e.getNombreArchivo();
        return new ArchivoDescargado(nombre, archivos.leer(e.getRutaArchivo()));
    }

    @Transactional
    public EntregaResponse modificar(Long id, EntregaActualizarRequest req) {
        Entrega e = buscar.entrega(id);
        if (e.estaCalificada()) {
            throw new ConflictoException("La entrega ya fue calificada y no se puede modificar");
        }
        verificarVentana(e.getActividad());
        String nombre = req.getNombreArchivo() != null ? req.getNombreArchivo() : e.getNombreArchivo();
        verificarContenido(e.getActividad(), req.getTextoRespuesta(), nombre);
        e.setTextoRespuesta(req.getTextoRespuesta());
        e.setNombreArchivo(nombre);
        e.setFechaEntrega(LocalDateTime.now());
        return Conversor.aEntrega(entregas.save(e));
    }

    /** Califica y retroalimenta. Solo el docente dueno del curso puede hacerlo. */
    @Transactional
    public EntregaResponse calificar(Long id, CalificacionRequest req) {
        Entrega e = buscar.entrega(id);
        Usuario docente = buscar.usuario(req.getDocenteId());
        Curso curso = e.getActividad().getUnidad().getCurso();
        if (!Roles.DOCENTE.equalsIgnoreCase(docente.getRol().getNombre())
                || !curso.getDocente().getId().equals(docente.getId())) {
            throw new ProhibidoException("Solo el docente de este curso puede calificar sus entregas");
        }
        e.setCalificacion(Numeros.redondear(req.getNota()));
        e.setRetroalimentacion(req.getRetroalimentacion());
        e.setFechaCalificacion(LocalDateTime.now());
        e = entregas.save(e);
        String detalle = "Nota: " + e.getCalificacion()
                + (e.getRetroalimentacion() == null ? "" : ". Retroalimentacion: " + e.getRetroalimentacion());
        notificaciones.crear(e.getEstudiante(), docente, TipoNotificacion.CALIFICACION,
                "Calificacion de " + e.getActividad().getTitulo(), detalle, curso.getId());
        return Conversor.aEntrega(e);
    }

    @Transactional
    public void eliminar(Long id) {
        Entrega e = buscar.entrega(id);
        if (e.estaCalificada()) {
            throw new ConflictoException("No se puede eliminar una entrega ya calificada");
        }
        archivos.eliminar(e.getRutaArchivo());
        entregas.delete(e);
    }

    // ------------------------------------------------------------- internos

    private Entrega registrar(EntregaRequest req) {
        Actividad act = buscar.actividad(req.getActividadId());
        Usuario est = buscar.usuario(req.getEstudianteId());
        Curso curso = act.getUnidad().getCurso();

        if (!Roles.ESTUDIANTE.equalsIgnoreCase(est.getRol().getNombre())) {
            throw new ProhibidoException("Solo los estudiantes pueden entregar actividades");
        }
        if (!matriculas.existsByEstudiante_IdAndCurso_Id(est.getId(), curso.getId())) {
            throw new ProhibidoException("El estudiante no esta matriculado en el curso de esta actividad");
        }
        verificarVentana(act);
        if (entregas.findByActividad_IdAndEstudiante_Id(act.getId(), est.getId()).isPresent()) {
            throw new ConflictoException("El estudiante ya entrego esta actividad. Use PUT para modificarla");
        }
        verificarContenido(act, req.getTextoRespuesta(), req.getNombreArchivo());
        verificarSecuencia(act, est);

        Entrega e = new Entrega();
        e.setActividad(act);
        e.setEstudiante(est);
        e.setTextoRespuesta(req.getTextoRespuesta());
        e.setNombreArchivo(req.getNombreArchivo());
        e.setFechaEntrega(LocalDateTime.now());
        e = entregas.save(e);
        notificaciones.crear(curso.getDocente(), est, TipoNotificacion.ENTREGA_RECIBIDA,
                "Entrega recibida: " + act.getTitulo(),
                est.getNombre() + " entrego la actividad '" + act.getTitulo() + "' del curso '" + curso.getTitulo() + "'.",
                curso.getId());
        return e;
    }

    /** Si la actividad es parte de un proceso formativo, solo se puede entregar cuando las anteriores ya se entregaron. */
    private void verificarSecuencia(Actividad act, Usuario est) {
        Optional<PasoProceso> paso = pasos.findByActividad_Id(act.getId());
        if (!paso.isPresent()) {
            return;
        }
        ProcesoFormativo proceso = paso.get().getProceso();
        List<PasoProceso> lista = pasos.findByProceso_IdOrderByOrdenAsc(proceso.getId());
        Map<Long, Entrega> mapa = new HashMap<>();
        for (PasoProceso p : lista) {
            Optional<Entrega> previa = entregas.findByActividad_IdAndEstudiante_Id(p.getActividad().getId(), est.getId());
            if (previa.isPresent()) {
                mapa.put(p.getActividad().getId(), previa.get());
            }
        }
        for (PasoProgreso pp : ProcesoServicio.calcularPasos(lista, mapa)) {
            if (pp.getActividadId().equals(act.getId()) && "BLOQUEADO".equals(pp.getEstado())) {
                String faltante = "una actividad anterior";
                for (PasoProceso p : lista) {
                    if (!mapa.containsKey(p.getActividad().getId())) {
                        faltante = "'" + p.getActividad().getTitulo() + "'";
                        break;
                    }
                }
                throw new ReglaNegocioException("Esta actividad es parte del proceso '" + proceso.getNombre()
                        + "' y debe hacerse en orden: primero entregue " + faltante);
            }
        }
    }

    private void verificarVentana(Actividad act) {
        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isBefore(act.getFechaInicio())) {
            throw new ReglaNegocioException("La actividad todavia no esta habilitada (abre el " + act.getFechaInicio() + ")");
        }
        if (ahora.isAfter(act.getFechaLimite())) {
            throw new ReglaNegocioException("El plazo de entrega vencio el " + act.getFechaLimite());
        }
    }

    private void verificarContenido(Actividad act, String texto, String archivo) {
        boolean sinTexto = texto == null || texto.trim().isEmpty();
        boolean sinArchivo = archivo == null || archivo.trim().isEmpty();
        if (act.getTipoEntrega() == TipoEntrega.ARCHIVO && sinArchivo) {
            throw new ReglaNegocioException("Esta actividad pide un archivo: adjuntelo (campo 'archivo') o envie nombreArchivo");
        }
        if ((act.getTipoEntrega() == TipoEntrega.TEXTO || act.getTipoEntrega() == TipoEntrega.PREGUNTAS) && sinTexto) {
            throw new ReglaNegocioException("Esta actividad pide una respuesta: envie textoRespuesta");
        }
    }
}
