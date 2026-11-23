package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.ContenidoRequest;
import co.edu.unipiloto.plataforma.dto.ContenidoResponse;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.ContenidoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contenidos que el docente publica en las unidades de su curso (archivos, videos, enlaces).
 * Los estudiantes matriculados pueden listarlos y descargar los archivos.
 */
@Service
public class ContenidoServicio {

    private final ContenidoRepositorio contenidos;
    private final ArchivoServicio archivos;
    private final NotificacionServicio notificaciones;
    private final Permisos permisos;
    private final Buscador buscar;

    public ContenidoServicio(ContenidoRepositorio contenidos, ArchivoServicio archivos,
                             NotificacionServicio notificaciones, Permisos permisos, Buscador buscar) {
        this.contenidos = contenidos;
        this.archivos = archivos;
        this.notificaciones = notificaciones;
        this.permisos = permisos;
        this.buscar = buscar;
    }

    @Transactional(readOnly = true)
    public List<ContenidoResponse> listar(Long unidadId, Long cursoId) {
        List<Contenido> base;
        if (unidadId != null) {
            buscar.unidad(unidadId);
            base = contenidos.findByUnidad_Id(unidadId);
        } else if (cursoId != null) {
            buscar.curso(cursoId);
            base = contenidos.findByUnidad_Curso_Id(cursoId);
        } else {
            base = contenidos.findAll();
        }
        List<ContenidoResponse> salida = new ArrayList<>();
        for (Contenido c : base) {
            salida.add(Conversor.aContenido(c));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public ContenidoResponse obtener(Long id) {
        return Conversor.aContenido(buscar2(id));
    }

    @Transactional
    public ContenidoResponse crear(ContenidoRequest req) {
        Unidad unidad = buscar.unidad(req.getUnidadId());
        Curso curso = permisos.exigirDocenteDelCurso(unidad.getCurso().getId(), req.getDocenteId(), "publicar contenidos");
        validarUrl(req);
        Contenido c = new Contenido();
        c.setUnidad(unidad);
        aplicar(req, c);
        c.setFechaPublicacion(LocalDateTime.now());
        c = contenidos.save(c);
        notificaciones.notificarMatriculados(curso, curso.getDocente(), TipoNotificacion.CONTENIDO_NUEVO,
                "Nuevo contenido en " + curso.getTitulo(), "Se publico '" + c.getTitulo() + "' en la unidad '"
                        + unidad.getTitulo() + "'.");
        return Conversor.aContenido(c);
    }

    @Transactional
    public ContenidoResponse actualizar(Long id, ContenidoRequest req) {
        Contenido c = buscar2(id);
        permisos.exigirDocenteDelCurso(c.getUnidad().getCurso().getId(), req.getDocenteId(), "modificar contenidos");
        validarUrl(req);
        Unidad unidad = buscar.unidad(req.getUnidadId());
        if (!unidad.getCurso().getId().equals(c.getUnidad().getCurso().getId())) {
            throw new ReglaNegocioException("La unidad debe pertenecer al mismo curso del contenido");
        }
        c.setUnidad(unidad);
        aplicar(req, c);
        return Conversor.aContenido(contenidos.save(c));
    }

    @Transactional
    public void eliminar(Long id, Long docenteId) {
        Contenido c = buscar2(id);
        permisos.exigirDocenteDelCurso(c.getUnidad().getCurso().getId(), docenteId, "eliminar contenidos");
        archivos.eliminar(c.getRutaArchivo());
        contenidos.delete(c);
    }

    /** El docente adjunta (o reemplaza) el archivo de un contenido. */
    @Transactional
    public ContenidoResponse subirArchivo(Long id, Long docenteId, MultipartFile archivo) {
        Contenido c = buscar2(id);
        permisos.exigirDocenteDelCurso(c.getUnidad().getCurso().getId(), docenteId, "subir archivos de contenido");
        String nombre = archivos.nombreSeguro(archivo == null ? null : archivo.getOriginalFilename());
        String nueva = archivos.guardar("contenidos", archivo);
        archivos.eliminar(c.getRutaArchivo());
        c.setRutaArchivo(nueva);
        c.setNombreArchivo(nombre);
        c.setTamanoBytes(archivo.getSize());
        return Conversor.aContenido(contenidos.save(c));
    }

    /** Descarga: docente del curso, coordinador o estudiante matriculado. */
    @Transactional(readOnly = true)
    public ArchivoDescargado descargar(Long id, Long usuarioId) {
        Contenido c = buscar2(id);
        Usuario u = buscar.usuario(usuarioId);
        if (!permisos.esMiembroDelCurso(c.getUnidad().getCurso(), u)) {
            throw new ProhibidoException("Solo los estudiantes matriculados, el docente o un coordinador pueden descargar este material");
        }
        if (c.getRutaArchivo() == null) {
            throw new RecursoNoEncontradoException("Este contenido no tiene archivo adjunto"
                    + (c.getUrl() != null ? " (usa el enlace: " + c.getUrl() + ")" : ""));
        }
        return new ArchivoDescargado(c.getNombreArchivo(), archivos.leer(c.getRutaArchivo()));
    }

    private Contenido buscar2(Long id) {
        return contenidos.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("El contenido", id));
    }

    private void validarUrl(ContenidoRequest req) {
        boolean sinUrl = req.getUrl() == null || req.getUrl().trim().isEmpty();
        if (req.getTipo() == TipoContenido.ENLACE && sinUrl) {
            throw new ReglaNegocioException("Un contenido de tipo ENLACE necesita la url");
        }
        if (!sinUrl && !req.getUrl().trim().toLowerCase().startsWith("http")) {
            throw new ReglaNegocioException("La url debe empezar por http:// o https://");
        }
    }

    private void aplicar(ContenidoRequest req, Contenido c) {
        c.setTitulo(req.getTitulo().trim());
        c.setDescripcion(req.getDescripcion());
        c.setTipo(req.getTipo());
        c.setUrl(req.getUrl() == null || req.getUrl().trim().isEmpty() ? null : req.getUrl().trim());
    }
}
