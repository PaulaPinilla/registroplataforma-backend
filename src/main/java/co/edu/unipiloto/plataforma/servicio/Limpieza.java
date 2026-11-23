package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.modelo.Actividad;
import co.edu.unipiloto.plataforma.modelo.Contenido;
import co.edu.unipiloto.plataforma.modelo.Entrega;
import co.edu.unipiloto.plataforma.modelo.Foro;
import co.edu.unipiloto.plataforma.modelo.Unidad;
import co.edu.unipiloto.plataforma.repositorio.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Borrado en cascada controlado: elimina primero lo que depende de un curso, unidad o actividad
 * (reclamos, pasos de procesos, entregas y sus archivos, contenidos, foros, certificados...).
 * Se hace aqui (y no con CascadeType.REMOVE) para que el orden sea explicito y facil de explicar.
 */
@Component
public class Limpieza {

    private final EntregaRepositorio entregas;
    private final ActividadRepositorio actividades;
    private final UnidadRepositorio unidades;
    private final MatriculaRepositorio matriculas;
    private final EvaluacionCursoRepositorio evaluaciones;
    private final ReclamoRepositorio reclamos;
    private final PasoProcesoRepositorio pasos;
    private final ProcesoFormativoRepositorio procesos;
    private final ContenidoRepositorio contenidos;
    private final ForoRepositorio foros;
    private final MensajeForoRepositorio mensajes;
    private final CertificadoRepositorio certificados;
    private final ArchivoServicio archivos;

    public Limpieza(EntregaRepositorio entregas, ActividadRepositorio actividades, UnidadRepositorio unidades,
                    MatriculaRepositorio matriculas, EvaluacionCursoRepositorio evaluaciones,
                    ReclamoRepositorio reclamos, PasoProcesoRepositorio pasos, ProcesoFormativoRepositorio procesos,
                    ContenidoRepositorio contenidos, ForoRepositorio foros, MensajeForoRepositorio mensajes,
                    CertificadoRepositorio certificados, ArchivoServicio archivos) {
        this.entregas = entregas;
        this.actividades = actividades;
        this.unidades = unidades;
        this.matriculas = matriculas;
        this.evaluaciones = evaluaciones;
        this.reclamos = reclamos;
        this.pasos = pasos;
        this.procesos = procesos;
        this.contenidos = contenidos;
        this.foros = foros;
        this.mensajes = mensajes;
        this.certificados = certificados;
        this.archivos = archivos;
    }

    @Transactional
    public void borrarActividad(Long actividadId) {
        limpiarActividad(actividadId);
        actividades.deleteById(actividadId);
    }

    @Transactional
    public void borrarUnidad(Long unidadId) {
        limpiarUnidad(unidadId);
        unidades.deleteById(unidadId);
    }

    @Transactional
    public void borrarCurso(Long cursoId) {
        for (Unidad u : unidades.findByCurso_IdOrderByOrdenAsc(cursoId)) {
            limpiarUnidad(u.getId());
        }
        unidades.deleteByCurso_Id(cursoId);
        procesos.deleteByCurso_Id(cursoId);
        for (Foro f : foros.findByCurso_Id(cursoId)) {
            mensajes.deleteByForo_Id(f.getId());
        }
        foros.deleteByCurso_Id(cursoId);
        certificados.deleteByCurso_Id(cursoId);
        matriculas.deleteByCurso_Id(cursoId);
        evaluaciones.deleteByCurso_Id(cursoId);
    }

    /** Borra lo que cuelga de las actividades y contenidos de una unidad (no la unidad). */
    private void limpiarUnidad(Long unidadId) {
        for (Actividad a : actividades.findByUnidad_Id(unidadId)) {
            limpiarActividad(a.getId());
        }
        actividades.deleteByUnidad_Id(unidadId);
        for (Contenido c : contenidos.findByUnidad_Id(unidadId)) {
            archivos.eliminar(c.getRutaArchivo());
        }
        contenidos.deleteByUnidad_Id(unidadId);
    }

    /** Borra reclamos, pasos de proceso y entregas (con sus archivos) de una actividad. */
    private void limpiarActividad(Long actividadId) {
        reclamos.deleteByEntrega_Actividad_Id(actividadId);
        pasos.deleteByActividad_Id(actividadId);
        for (Entrega e : entregas.findByActividad_Id(actividadId)) {
            archivos.eliminar(e.getRutaArchivo());
        }
        entregas.deleteByActividad_Id(actividadId);
    }
}
