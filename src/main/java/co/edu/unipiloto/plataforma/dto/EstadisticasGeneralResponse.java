package co.edu.unipiloto.plataforma.dto;

import java.util.List;

public class EstadisticasGeneralResponse {

    private long totalUsuarios;

    private long totalDocentes;

    private long totalEstudiantes;

    private long totalOfertas;

    private long totalCursos;

    private long totalUnidades;

    private long totalActividades;

    private long totalMatriculas;

    private long totalEntregas;

    private long entregasCalificadas;

    private long totalEvaluaciones;

    private Double promedioEvaluacionCursos;

    private Double promedioNotasGeneral;

    private List<CursoPopular> cursosMasMatriculados;

    public long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(long totalUsuarios) { this.totalUsuarios = totalUsuarios; }

    public long getTotalDocentes() { return totalDocentes; }
    public void setTotalDocentes(long totalDocentes) { this.totalDocentes = totalDocentes; }

    public long getTotalEstudiantes() { return totalEstudiantes; }
    public void setTotalEstudiantes(long totalEstudiantes) { this.totalEstudiantes = totalEstudiantes; }

    public long getTotalOfertas() { return totalOfertas; }
    public void setTotalOfertas(long totalOfertas) { this.totalOfertas = totalOfertas; }

    public long getTotalCursos() { return totalCursos; }
    public void setTotalCursos(long totalCursos) { this.totalCursos = totalCursos; }

    public long getTotalUnidades() { return totalUnidades; }
    public void setTotalUnidades(long totalUnidades) { this.totalUnidades = totalUnidades; }

    public long getTotalActividades() { return totalActividades; }
    public void setTotalActividades(long totalActividades) { this.totalActividades = totalActividades; }

    public long getTotalMatriculas() { return totalMatriculas; }
    public void setTotalMatriculas(long totalMatriculas) { this.totalMatriculas = totalMatriculas; }

    public long getTotalEntregas() { return totalEntregas; }
    public void setTotalEntregas(long totalEntregas) { this.totalEntregas = totalEntregas; }

    public long getEntregasCalificadas() { return entregasCalificadas; }
    public void setEntregasCalificadas(long entregasCalificadas) { this.entregasCalificadas = entregasCalificadas; }

    public long getTotalEvaluaciones() { return totalEvaluaciones; }
    public void setTotalEvaluaciones(long totalEvaluaciones) { this.totalEvaluaciones = totalEvaluaciones; }

    public Double getPromedioEvaluacionCursos() { return promedioEvaluacionCursos; }
    public void setPromedioEvaluacionCursos(Double promedioEvaluacionCursos) { this.promedioEvaluacionCursos = promedioEvaluacionCursos; }

    public Double getPromedioNotasGeneral() { return promedioNotasGeneral; }
    public void setPromedioNotasGeneral(Double promedioNotasGeneral) { this.promedioNotasGeneral = promedioNotasGeneral; }

    public List<CursoPopular> getCursosMasMatriculados() { return cursosMasMatriculados; }
    public void setCursosMasMatriculados(List<CursoPopular> cursosMasMatriculados) { this.cursosMasMatriculados = cursosMasMatriculados; }

}
