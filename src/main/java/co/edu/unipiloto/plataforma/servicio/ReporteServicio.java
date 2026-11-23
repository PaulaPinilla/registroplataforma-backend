package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.EstadisticasCursoResponse;
import co.edu.unipiloto.plataforma.dto.ReporteCursoLinea;
import co.edu.unipiloto.plataforma.dto.ReportePlataformaResponse;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.CertificadoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.CursoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.NotificacionRepositorio;
import co.edu.unipiloto.plataforma.repositorio.ReclamoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Reporte de la plataforma para el coordinador: estadisticas generales y una linea por curso. */
@Service
public class ReporteServicio {

    private final CursoRepositorio cursos;
    private final ReclamoRepositorio reclamos;
    private final CertificadoRepositorio certificados;
    private final NotificacionRepositorio notificaciones;
    private final EstadisticaServicio estadisticas;
    private final Permisos permisos;

    public ReporteServicio(CursoRepositorio cursos, ReclamoRepositorio reclamos, CertificadoRepositorio certificados,
                           NotificacionRepositorio notificaciones, EstadisticaServicio estadisticas, Permisos permisos) {
        this.cursos = cursos;
        this.reclamos = reclamos;
        this.certificados = certificados;
        this.notificaciones = notificaciones;
        this.estadisticas = estadisticas;
        this.permisos = permisos;
    }

    @Transactional(readOnly = true)
    public ReportePlataformaResponse plataforma(Long coordinadorId) {
        permisos.exigirRol(coordinadorId, Roles.COORDINADOR, "consultar los reportes de la plataforma");
        List<ReporteCursoLinea> lineas = new ArrayList<>();
        long publicados = 0;
        for (Curso c : cursos.findAll()) {
            EstadisticasCursoResponse e = estadisticas.deCurso(c.getId());
            ReporteCursoLinea l = new ReporteCursoLinea();
            l.setCursoId(c.getId());
            l.setTitulo(c.getTitulo());
            l.setDocenteNombre(c.getDocente().getNombre());
            l.setPublicado(c.estaPublicado());
            l.setCupos(c.getCupos());
            l.setMatriculados(e.getEstudiantesMatriculados());
            l.setCuposDisponibles(c.getCupos() == null ? null : Long.valueOf(Math.max(0, c.getCupos() - e.getEstudiantesMatriculados())));
            l.setAprobados(e.getAprobados());
            l.setReprobados(e.getReprobados());
            l.setEnCurso(e.getEnCurso());
            l.setPromedioNotaFinal(e.getPromedioNotaFinal());
            l.setEvaluacionPromedio(e.getEvaluacionPromedioCurso());
            lineas.add(l);
            if (c.estaPublicado()) {
                publicados++;
            }
        }
        ReportePlataformaResponse r = new ReportePlataformaResponse();
        r.setGeneradoEn(LocalDateTime.now());
        r.setGeneral(estadisticas.general());
        r.setCursos(lineas);
        r.setCursosPublicados(publicados);
        r.setReclamosAbiertos(reclamos.countByEstado(EstadoReclamo.ABIERTO));
        r.setCertificadosEmitidos(certificados.countByEstado(EstadoCertificado.EMITIDO));
        r.setNotificacionesEnviadas(notificaciones.count());
        return r;
    }
}
