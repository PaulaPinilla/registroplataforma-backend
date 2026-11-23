package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.CertificadoRequest;
import co.edu.unipiloto.plataforma.dto.CertificadoResponse;
import co.edu.unipiloto.plataforma.dto.ProgresoCursoResponse;
import co.edu.unipiloto.plataforma.dto.VerificacionCertificadoResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ProhibidoException;
import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.CertificadoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Solicitud de certificados: solo un estudiante matriculado que ya aprobo el curso
 * (todas las actividades calificadas y nota final de 3.0 o mas). El certificado tiene un codigo verificable.
 */
@Service
public class CertificadoServicio {

    private final CertificadoRepositorio certificados;
    private final MatriculaRepositorio matriculas;
    private final ProgresoServicio progreso;
    private final NotificacionServicio notificaciones;
    private final Buscador buscar;

    public CertificadoServicio(CertificadoRepositorio certificados, MatriculaRepositorio matriculas,
                               ProgresoServicio progreso, NotificacionServicio notificaciones, Buscador buscar) {
        this.certificados = certificados;
        this.matriculas = matriculas;
        this.progreso = progreso;
        this.notificaciones = notificaciones;
        this.buscar = buscar;
    }

    @Transactional
    public CertificadoResponse solicitar(CertificadoRequest req) {
        Usuario est = buscar.usuario(req.getEstudianteId());
        Curso curso = buscar.curso(req.getCursoId());
        if (!Roles.ESTUDIANTE.equalsIgnoreCase(est.getRol().getNombre())) {
            throw new ProhibidoException("Solo los estudiantes pueden solicitar certificados");
        }
        if (!matriculas.existsByEstudiante_IdAndCurso_Id(est.getId(), curso.getId())) {
            throw new ProhibidoException("El estudiante no esta matriculado en este curso");
        }
        Optional<Certificado> previo = certificados.findByEstudiante_IdAndCurso_Id(est.getId(), curso.getId());
        if (previo.isPresent()) {
            throw new ConflictoException("Ya existe un certificado para este curso. Codigo: " + previo.get().getCodigo());
        }
        ProgresoCursoResponse p = progreso.deEstudiante(curso.getId(), est.getId());
        if (!"APROBADO".equals(p.getEstado())) {
            String motivo = p.getNotaFinal() == null
                    ? "aun hay actividades sin calificar (" + p.getCalificadas() + " de " + p.getTotalActividades() + " calificadas)"
                    : "la nota final es " + p.getNotaFinal() + " y se necesita " + ProgresoServicio.NOTA_APROBACION + " o mas";
            throw new ReglaNegocioException("No es posible emitir el certificado: " + motivo);
        }
        Certificado c = new Certificado();
        c.setEstudiante(est);
        c.setCurso(curso);
        c.setCodigo("CERT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        c.setNotaFinal(p.getNotaFinal());
        c.setEstado(EstadoCertificado.EMITIDO);
        c.setFechaSolicitud(LocalDateTime.now());
        c.setFechaEmision(LocalDateTime.now());
        c = certificados.save(c);
        notificaciones.crear(est, curso.getDocente(), TipoNotificacion.CERTIFICADO,
                "Certificado emitido: " + curso.getTitulo(),
                "Su certificado fue emitido con la nota " + c.getNotaFinal() + ". Codigo de verificacion: " + c.getCodigo(),
                curso.getId());
        return Conversor.aCertificado(c);
    }

    @Transactional(readOnly = true)
    public List<CertificadoResponse> listar(Long estudianteId, Long cursoId) {
        List<Certificado> base;
        if (estudianteId != null) {
            base = certificados.findByEstudiante_Id(estudianteId);
        } else if (cursoId != null) {
            base = certificados.findByCurso_Id(cursoId);
        } else {
            base = certificados.findAll();
        }
        List<CertificadoResponse> salida = new ArrayList<>();
        for (Certificado c : base) {
            salida.add(Conversor.aCertificado(c));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public CertificadoResponse obtener(Long id) {
        return Conversor.aCertificado(certificado(id));
    }

    /** Texto de la constancia, listo para mostrar o imprimir. */
    @Transactional(readOnly = true)
    public String documento(Long id) {
        Certificado c = certificado(id);
        StringBuilder sb = new StringBuilder();
        sb.append("CERTIFICADO DE APROBACION\n");
        sb.append("=========================\n\n");
        sb.append("Se certifica que ").append(c.getEstudiante().getNombre()).append("\n");
        sb.append("aprobo el curso \"").append(c.getCurso().getTitulo()).append("\"\n");
        sb.append("(semestre ").append(c.getCurso().getSemestre()).append(")\n");
        sb.append("con una nota final de ").append(c.getNotaFinal()).append(" sobre 5.0.\n\n");
        sb.append("Docente: ").append(c.getCurso().getDocente().getNombre()).append("\n");
        sb.append("Fecha de emision: ").append(c.getFechaEmision()).append("\n");
        sb.append("Codigo de verificacion: ").append(c.getCodigo()).append("\n");
        return sb.toString();
    }

    /** Verificacion publica: no revela datos si el codigo no existe. */
    @Transactional(readOnly = true)
    public VerificacionCertificadoResponse verificar(String codigo) {
        VerificacionCertificadoResponse r = new VerificacionCertificadoResponse();
        r.setCodigo(codigo);
        Optional<Certificado> c = certificados.findByCodigo(codigo);
        if (!c.isPresent() || c.get().getEstado() != EstadoCertificado.EMITIDO) {
            r.setValido(false);
            r.setMensaje("El codigo no corresponde a ningun certificado emitido");
            return r;
        }
        r.setValido(true);
        r.setEstudianteNombre(c.get().getEstudiante().getNombre());
        r.setCursoTitulo(c.get().getCurso().getTitulo());
        r.setNotaFinal(c.get().getNotaFinal());
        r.setFechaEmision(c.get().getFechaEmision());
        r.setMensaje("Certificado valido");
        return r;
    }

    private Certificado certificado(Long id) {
        return certificados.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("El certificado", id));
    }
}
