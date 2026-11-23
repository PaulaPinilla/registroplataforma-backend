package co.edu.unipiloto.plataforma.dto;

public class ReporteCursoLinea {

    private Long cursoId;

    private String titulo;

    private String docenteNombre;

    private boolean publicado;

    private Integer cupos;

    private int matriculados;

    private Long cuposDisponibles;

    private int aprobados;

    private int reprobados;

    private int enCurso;

    private Double promedioNotaFinal;

    private Double evaluacionPromedio;

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDocenteNombre() { return docenteNombre; }
    public void setDocenteNombre(String docenteNombre) { this.docenteNombre = docenteNombre; }

    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }

    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }

    public int getMatriculados() { return matriculados; }
    public void setMatriculados(int matriculados) { this.matriculados = matriculados; }

    public Long getCuposDisponibles() { return cuposDisponibles; }
    public void setCuposDisponibles(Long cuposDisponibles) { this.cuposDisponibles = cuposDisponibles; }

    public int getAprobados() { return aprobados; }
    public void setAprobados(int aprobados) { this.aprobados = aprobados; }

    public int getReprobados() { return reprobados; }
    public void setReprobados(int reprobados) { this.reprobados = reprobados; }

    public int getEnCurso() { return enCurso; }
    public void setEnCurso(int enCurso) { this.enCurso = enCurso; }

    public Double getPromedioNotaFinal() { return promedioNotaFinal; }
    public void setPromedioNotaFinal(Double promedioNotaFinal) { this.promedioNotaFinal = promedioNotaFinal; }

    public Double getEvaluacionPromedio() { return evaluacionPromedio; }
    public void setEvaluacionPromedio(Double evaluacionPromedio) { this.evaluacionPromedio = evaluacionPromedio; }

}
