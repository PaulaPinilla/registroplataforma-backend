package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.TipoContenido;
import java.time.LocalDateTime;

public class ContenidoResponse {

    private Long id;

    private String titulo;

    private String descripcion;

    private TipoContenido tipo;

    private String url;

    private boolean tieneArchivo;

    private String nombreArchivo;

    private Long tamanoBytes;

    private LocalDateTime fechaPublicacion;

    private Long unidadId;

    private String unidadTitulo;

    private Long cursoId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public TipoContenido getTipo() { return tipo; }
    public void setTipo(TipoContenido tipo) { this.tipo = tipo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public boolean isTieneArchivo() { return tieneArchivo; }
    public void setTieneArchivo(boolean tieneArchivo) { this.tieneArchivo = tieneArchivo; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public Long getTamanoBytes() { return tamanoBytes; }
    public void setTamanoBytes(Long tamanoBytes) { this.tamanoBytes = tamanoBytes; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public Long getUnidadId() { return unidadId; }
    public void setUnidadId(Long unidadId) { this.unidadId = unidadId; }

    public String getUnidadTitulo() { return unidadTitulo; }
    public void setUnidadTitulo(String unidadTitulo) { this.unidadTitulo = unidadTitulo; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

}
