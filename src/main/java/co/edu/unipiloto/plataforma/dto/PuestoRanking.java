package co.edu.unipiloto.plataforma.dto;

public class PuestoRanking {

    private Long estudianteId;

    private String nombre;

    private double notaAcumulada;

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getNotaAcumulada() { return notaAcumulada; }
    public void setNotaAcumulada(double notaAcumulada) { this.notaAcumulada = notaAcumulada; }

}
