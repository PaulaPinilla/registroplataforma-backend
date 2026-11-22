package co.edu.unipiloto.plataforma.dto;

import java.util.List;

public class CorteProgreso {

    private int corte;

    private double pesoCorte;

    private double porcentajeAsignado;

    private double notaCorte;

    private List<ActividadProgreso> actividades;

    public int getCorte() { return corte; }
    public void setCorte(int corte) { this.corte = corte; }

    public double getPesoCorte() { return pesoCorte; }
    public void setPesoCorte(double pesoCorte) { this.pesoCorte = pesoCorte; }

    public double getPorcentajeAsignado() { return porcentajeAsignado; }
    public void setPorcentajeAsignado(double porcentajeAsignado) { this.porcentajeAsignado = porcentajeAsignado; }

    public double getNotaCorte() { return notaCorte; }
    public void setNotaCorte(double notaCorte) { this.notaCorte = notaCorte; }

    public List<ActividadProgreso> getActividades() { return actividades; }
    public void setActividades(List<ActividadProgreso> actividades) { this.actividades = actividades; }

}
