package co.edu.unipiloto.plataforma.dto;

public class PasoResponse {

    private int orden;

    private Long actividadId;

    private String actividadTitulo;

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }

    public String getActividadTitulo() { return actividadTitulo; }
    public void setActividadTitulo(String actividadTitulo) { this.actividadTitulo = actividadTitulo; }

}
