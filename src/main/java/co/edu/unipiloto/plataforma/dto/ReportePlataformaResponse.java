package co.edu.unipiloto.plataforma.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ReportePlataformaResponse {

    private LocalDateTime generadoEn;

    private EstadisticasGeneralResponse general;

    private List<ReporteCursoLinea> cursos;

    private long cursosPublicados;

    private long reclamosAbiertos;

    private long certificadosEmitidos;

    private long notificacionesEnviadas;

    public LocalDateTime getGeneradoEn() { return generadoEn; }
    public void setGeneradoEn(LocalDateTime generadoEn) { this.generadoEn = generadoEn; }

    public EstadisticasGeneralResponse getGeneral() { return general; }
    public void setGeneral(EstadisticasGeneralResponse general) { this.general = general; }

    public List<ReporteCursoLinea> getCursos() { return cursos; }
    public void setCursos(List<ReporteCursoLinea> cursos) { this.cursos = cursos; }

    public long getCursosPublicados() { return cursosPublicados; }
    public void setCursosPublicados(long cursosPublicados) { this.cursosPublicados = cursosPublicados; }

    public long getReclamosAbiertos() { return reclamosAbiertos; }
    public void setReclamosAbiertos(long reclamosAbiertos) { this.reclamosAbiertos = reclamosAbiertos; }

    public long getCertificadosEmitidos() { return certificadosEmitidos; }
    public void setCertificadosEmitidos(long certificadosEmitidos) { this.certificadosEmitidos = certificadosEmitidos; }

    public long getNotificacionesEnviadas() { return notificacionesEnviadas; }
    public void setNotificacionesEnviadas(long notificacionesEnviadas) { this.notificacionesEnviadas = notificacionesEnviadas; }

}
