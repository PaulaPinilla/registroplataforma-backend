package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.Categoria;
import java.time.LocalDateTime;

/**
 * Estado de una actividad para un estudiante: SIN_ENTREGAR, VENCIDA, ENTREGADA o CALIFICADA.
 */
public class ActividadProgreso {

    private Long actividadId;

    private String titulo;

    private Categoria categoria;

    private double porcentaje;

    private LocalDateTime fechaLimite;

    private String estado;

    private Double nota;

    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }

    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

}
