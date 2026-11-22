package co.edu.unipiloto.plataforma.dto;

import java.util.List;
import java.util.Map;

public class EstadisticasCursoResponse {

    private Long cursoId;

    private String cursoTitulo;

    private int estudiantesMatriculados;

    private int totalActividades;

    private int entregasRecibidas;

    private int entregasCalificadas;

    private int entregasPorCalificar;

    private double tasaEntregaPorcentaje;

    private Double promedioNotasEntregas;

    private double promedioAvancePorcentaje;

    private int aprobados;

    private int reprobados;

    private int enCurso;

    private Double promedioNotaFinal;

    private Double evaluacionPromedioCurso;

    private int numeroEvaluaciones;

    private Map<String, Integer> distribucionNotaFinal;

    private List<PuestoRanking> mejoresEstudiantes;

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getCursoTitulo() { return cursoTitulo; }
    public void setCursoTitulo(String cursoTitulo) { this.cursoTitulo = cursoTitulo; }

    public int getEstudiantesMatriculados() { return estudiantesMatriculados; }
    public void setEstudiantesMatriculados(int estudiantesMatriculados) { this.estudiantesMatriculados = estudiantesMatriculados; }

    public int getTotalActividades() { return totalActividades; }
    public void setTotalActividades(int totalActividades) { this.totalActividades = totalActividades; }

    public int getEntregasRecibidas() { return entregasRecibidas; }
    public void setEntregasRecibidas(int entregasRecibidas) { this.entregasRecibidas = entregasRecibidas; }

    public int getEntregasCalificadas() { return entregasCalificadas; }
    public void setEntregasCalificadas(int entregasCalificadas) { this.entregasCalificadas = entregasCalificadas; }

    public int getEntregasPorCalificar() { return entregasPorCalificar; }
    public void setEntregasPorCalificar(int entregasPorCalificar) { this.entregasPorCalificar = entregasPorCalificar; }

    public double getTasaEntregaPorcentaje() { return tasaEntregaPorcentaje; }
    public void setTasaEntregaPorcentaje(double tasaEntregaPorcentaje) { this.tasaEntregaPorcentaje = tasaEntregaPorcentaje; }

    public Double getPromedioNotasEntregas() { return promedioNotasEntregas; }
    public void setPromedioNotasEntregas(Double promedioNotasEntregas) { this.promedioNotasEntregas = promedioNotasEntregas; }

    public double getPromedioAvancePorcentaje() { return promedioAvancePorcentaje; }
    public void setPromedioAvancePorcentaje(double promedioAvancePorcentaje) { this.promedioAvancePorcentaje = promedioAvancePorcentaje; }

    public int getAprobados() { return aprobados; }
    public void setAprobados(int aprobados) { this.aprobados = aprobados; }

    public int getReprobados() { return reprobados; }
    public void setReprobados(int reprobados) { this.reprobados = reprobados; }

    public int getEnCurso() { return enCurso; }
    public void setEnCurso(int enCurso) { this.enCurso = enCurso; }

    public Double getPromedioNotaFinal() { return promedioNotaFinal; }
    public void setPromedioNotaFinal(Double promedioNotaFinal) { this.promedioNotaFinal = promedioNotaFinal; }

    public Double getEvaluacionPromedioCurso() { return evaluacionPromedioCurso; }
    public void setEvaluacionPromedioCurso(Double evaluacionPromedioCurso) { this.evaluacionPromedioCurso = evaluacionPromedioCurso; }

    public int getNumeroEvaluaciones() { return numeroEvaluaciones; }
    public void setNumeroEvaluaciones(int numeroEvaluaciones) { this.numeroEvaluaciones = numeroEvaluaciones; }

    public Map<String, Integer> getDistribucionNotaFinal() { return distribucionNotaFinal; }
    public void setDistribucionNotaFinal(Map<String, Integer> distribucionNotaFinal) { this.distribucionNotaFinal = distribucionNotaFinal; }

    public List<PuestoRanking> getMejoresEstudiantes() { return mejoresEstudiantes; }
    public void setMejoresEstudiantes(List<PuestoRanking> mejoresEstudiantes) { this.mejoresEstudiantes = mejoresEstudiantes; }

}
