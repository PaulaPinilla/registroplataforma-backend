package co.edu.unipiloto.plataforma.dto;

/**
 * Estado: BLOQUEADO, DISPONIBLE, ENTREGADA o CALIFICADA.
 */
public class PasoProgreso {

    private int orden;

    private Long actividadId;

    private String titulo;

    private String estado;

    private Double nota;

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

}
