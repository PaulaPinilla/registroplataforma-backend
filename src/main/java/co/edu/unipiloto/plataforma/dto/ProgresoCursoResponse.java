package co.edu.unipiloto.plataforma.dto;

import java.util.List;

public class ProgresoCursoResponse {

    private Long cursoId;

    private String cursoTitulo;

    private Long estudianteId;

    private String estudianteNombre;

    private boolean cursoFinalizado;

    private String modoCalculo;

    private int totalActividades;

    private int entregadas;

    private int calificadas;

    private int pendientesPorEntregar;

    private double porcentajeAvance;

    private double notaAcumulada;

    private Double notaFinal;

    private String estado;

    private List<CorteProgreso> cortes;

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getCursoTitulo() { return cursoTitulo; }
    public void setCursoTitulo(String cursoTitulo) { this.cursoTitulo = cursoTitulo; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public boolean isCursoFinalizado() { return cursoFinalizado; }
    public void setCursoFinalizado(boolean cursoFinalizado) { this.cursoFinalizado = cursoFinalizado; }

    public String getModoCalculo() { return modoCalculo; }
    public void setModoCalculo(String modoCalculo) { this.modoCalculo = modoCalculo; }

    public int getTotalActividades() { return totalActividades; }
    public void setTotalActividades(int totalActividades) { this.totalActividades = totalActividades; }

    public int getEntregadas() { return entregadas; }
    public void setEntregadas(int entregadas) { this.entregadas = entregadas; }

    public int getCalificadas() { return calificadas; }
    public void setCalificadas(int calificadas) { this.calificadas = calificadas; }

    public int getPendientesPorEntregar() { return pendientesPorEntregar; }
    public void setPendientesPorEntregar(int pendientesPorEntregar) { this.pendientesPorEntregar = pendientesPorEntregar; }

    public double getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(double porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }

    public double getNotaAcumulada() { return notaAcumulada; }
    public void setNotaAcumulada(double notaAcumulada) { this.notaAcumulada = notaAcumulada; }

    public Double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(Double notaFinal) { this.notaFinal = notaFinal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<CorteProgreso> getCortes() { return cortes; }
    public void setCortes(List<CorteProgreso> cortes) { this.cortes = cortes; }

}
