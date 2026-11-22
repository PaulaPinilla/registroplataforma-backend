package co.edu.unipiloto.plataforma.dto;

public class CursoPopular {

    private Long cursoId;

    private String titulo;

    private long matriculados;

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public long getMatriculados() { return matriculados; }
    public void setMatriculados(long matriculados) { this.matriculados = matriculados; }

}
