package co.edu.unipiloto.plataforma.dto;

public class CortePorcentaje {

    private int corte;

    private double pesoCorte;

    private int actividades;

    private double porcentajeAsignado;

    private boolean completo;

    public int getCorte() { return corte; }
    public void setCorte(int corte) { this.corte = corte; }

    public double getPesoCorte() { return pesoCorte; }
    public void setPesoCorte(double pesoCorte) { this.pesoCorte = pesoCorte; }

    public int getActividades() { return actividades; }
    public void setActividades(int actividades) { this.actividades = actividades; }

    public double getPorcentajeAsignado() { return porcentajeAsignado; }
    public void setPorcentajeAsignado(double porcentajeAsignado) { this.porcentajeAsignado = porcentajeAsignado; }

    public boolean isCompleto() { return completo; }
    public void setCompleto(boolean completo) { this.completo = completo; }

}
