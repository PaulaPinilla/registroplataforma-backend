package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.Categoria;
import co.edu.unipiloto.plataforma.modelo.TipoEntrega;
import java.time.LocalDateTime;

public class ActividadResponse {

    private Long id;

    private String titulo;

    private String instrucciones;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaLimite;

    private TipoEntrega tipoEntrega;

    private Categoria categoria;

    private int corte;

    private double porcentaje;

    private LocalDateTime fechaCreacion;

    private Long unidadId;

    private String unidadTitulo;

    private Long cursoId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }

    public TipoEntrega getTipoEntrega() { return tipoEntrega; }
    public void setTipoEntrega(TipoEntrega tipoEntrega) { this.tipoEntrega = tipoEntrega; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public int getCorte() { return corte; }
    public void setCorte(int corte) { this.corte = corte; }

    public double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Long getUnidadId() { return unidadId; }
    public void setUnidadId(Long unidadId) { this.unidadId = unidadId; }

    public String getUnidadTitulo() { return unidadTitulo; }
    public void setUnidadTitulo(String unidadTitulo) { this.unidadTitulo = unidadTitulo; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

}
