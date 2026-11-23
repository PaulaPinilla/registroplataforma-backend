package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.TipoContenido;
import javax.validation.constraints.*;

public class ContenidoRequest {

    @NotNull(message = "El docenteId es obligatorio")
    private Long docenteId;

    @NotNull(message = "El unidadId es obligatorio")
    private Long unidadId;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150, message = "El titulo admite maximo 150 caracteres")
    private String titulo;

    @Size(max = 1000, message = "La descripcion admite maximo 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El tipo es obligatorio (DOCUMENTO, PRESENTACION, VIDEO, ENLACE o LECTURA)")
    private TipoContenido tipo;

    @Size(max = 500, message = "La URL admite maximo 500 caracteres")
    private String url;

    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }

    public Long getUnidadId() { return unidadId; }
    public void setUnidadId(Long unidadId) { this.unidadId = unidadId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public TipoContenido getTipo() { return tipo; }
    public void setTipo(TipoContenido tipo) { this.tipo = tipo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

}
