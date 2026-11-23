package co.edu.unipiloto.plataforma.dto;

import java.time.LocalDateTime;

public class MensajeResponse {

    private Long id;

    private Long foroId;

    private Long autorId;

    private String autorNombre;

    private String autorRol;

    private String contenido;

    private Long respuestaAId;

    private LocalDateTime fecha;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getForoId() { return foroId; }
    public void setForoId(Long foroId) { this.foroId = foroId; }

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }

    public String getAutorNombre() { return autorNombre; }
    public void setAutorNombre(String autorNombre) { this.autorNombre = autorNombre; }

    public String getAutorRol() { return autorRol; }
    public void setAutorRol(String autorRol) { this.autorRol = autorRol; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Long getRespuestaAId() { return respuestaAId; }
    public void setRespuestaAId(Long respuestaAId) { this.respuestaAId = respuestaAId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

}
