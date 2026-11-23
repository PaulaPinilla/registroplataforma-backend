package co.edu.unipiloto.plataforma.dto;

import java.util.List;

public class ProcesoResponse {

    private Long id;

    private Long cursoId;

    private String nombre;

    private String descripcion;

    private List<PasoResponse> pasos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<PasoResponse> getPasos() { return pasos; }
    public void setPasos(List<PasoResponse> pasos) { this.pasos = pasos; }

}
