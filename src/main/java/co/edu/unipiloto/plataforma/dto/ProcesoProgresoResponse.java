package co.edu.unipiloto.plataforma.dto;

import java.util.List;

public class ProcesoProgresoResponse {

    private Long procesoId;

    private String nombre;

    private Long estudianteId;

    private int pasosTotales;

    private int pasosEntregados;

    private double porcentajeCompletado;

    private Long siguienteActividadId;

    private List<PasoProgreso> pasos;

    public Long getProcesoId() { return procesoId; }
    public void setProcesoId(Long procesoId) { this.procesoId = procesoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public int getPasosTotales() { return pasosTotales; }
    public void setPasosTotales(int pasosTotales) { this.pasosTotales = pasosTotales; }

    public int getPasosEntregados() { return pasosEntregados; }
    public void setPasosEntregados(int pasosEntregados) { this.pasosEntregados = pasosEntregados; }

    public double getPorcentajeCompletado() { return porcentajeCompletado; }
    public void setPorcentajeCompletado(double porcentajeCompletado) { this.porcentajeCompletado = porcentajeCompletado; }

    public Long getSiguienteActividadId() { return siguienteActividadId; }
    public void setSiguienteActividadId(Long siguienteActividadId) { this.siguienteActividadId = siguienteActividadId; }

    public List<PasoProgreso> getPasos() { return pasos; }
    public void setPasos(List<PasoProgreso> pasos) { this.pasos = pasos; }

}
