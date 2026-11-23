package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.TipoForo;
import java.time.LocalDateTime;

public class ForoResponse {

    private Long id;

    private Long cursoId;

    private String titulo;

    private String descripcion;

    private TipoForo tipo;

    private boolean abierto;

    private Long creadorId;

    private String creadorNombre;

    private LocalDateTime fechaCreacion;

    private long totalMensajes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public TipoForo getTipo() { return tipo; }
    public void setTipo(TipoForo tipo) { this.tipo = tipo; }

    public boolean isAbierto() { return abierto; }
    public void setAbierto(boolean abierto) { this.abierto = abierto; }

    public Long getCreadorId() { return creadorId; }
    public void setCreadorId(Long creadorId) { this.creadorId = creadorId; }

    public String getCreadorNombre() { return creadorNombre; }
    public void setCreadorNombre(String creadorNombre) { this.creadorNombre = creadorNombre; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public long getTotalMensajes() { return totalMensajes; }
    public void setTotalMensajes(long totalMensajes) { this.totalMensajes = totalMensajes; }

}
