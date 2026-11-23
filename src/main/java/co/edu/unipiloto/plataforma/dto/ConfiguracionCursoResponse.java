package co.edu.unipiloto.plataforma.dto;

import java.util.List;

/**
 * Resumen de la configuracion de un curso: que le falta para estar completo.
 */
public class ConfiguracionCursoResponse {

    private Long cursoId;

    private String titulo;

    private boolean pesosValidos;

    private int totalUnidades;

    private int totalActividades;

    private int totalContenidos;

    private List<CortePorcentaje> cortes;

    private boolean listoParaPublicar;

    private boolean publicado;

    private List<String> pendientes;

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public boolean isPesosValidos() { return pesosValidos; }
    public void setPesosValidos(boolean pesosValidos) { this.pesosValidos = pesosValidos; }

    public int getTotalUnidades() { return totalUnidades; }
    public void setTotalUnidades(int totalUnidades) { this.totalUnidades = totalUnidades; }

    public int getTotalActividades() { return totalActividades; }
    public void setTotalActividades(int totalActividades) { this.totalActividades = totalActividades; }

    public int getTotalContenidos() { return totalContenidos; }
    public void setTotalContenidos(int totalContenidos) { this.totalContenidos = totalContenidos; }

    public List<CortePorcentaje> getCortes() { return cortes; }
    public void setCortes(List<CortePorcentaje> cortes) { this.cortes = cortes; }

    public boolean isListoParaPublicar() { return listoParaPublicar; }
    public void setListoParaPublicar(boolean listoParaPublicar) { this.listoParaPublicar = listoParaPublicar; }

    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }

    public List<String> getPendientes() { return pendientes; }
    public void setPendientes(List<String> pendientes) { this.pendientes = pendientes; }

}
