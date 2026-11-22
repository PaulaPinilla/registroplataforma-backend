package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.EstadoOferta;
import java.time.LocalDate;

public class OfertaResponse {

    private Long id;

    private String nombre;

    private String descripcion;

    private String modalidad;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Integer cupos;

    private EstadoOferta estado;

    private long matriculasActuales;

    private long cuposDisponibles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }

    public EstadoOferta getEstado() { return estado; }
    public void setEstado(EstadoOferta estado) { this.estado = estado; }

    public long getMatriculasActuales() { return matriculasActuales; }
    public void setMatriculasActuales(long matriculasActuales) { this.matriculasActuales = matriculasActuales; }

    public long getCuposDisponibles() { return cuposDisponibles; }
    public void setCuposDisponibles(long cuposDisponibles) { this.cuposDisponibles = cuposDisponibles; }

}
