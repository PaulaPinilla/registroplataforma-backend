package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.EstadoOferta;
import java.time.LocalDate;
import javax.validation.constraints.*;

public class OfertaRequest {

    @NotBlank(message = "El nombre de la oferta es obligatorio")
    @Size(max = 150, message = "El nombre admite maximo 150 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripcion admite maximo 1000 caracteres")
    private String descripcion;

    @Size(max = 30, message = "La modalidad admite maximo 30 caracteres")
    private String modalidad;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @NotNull(message = "Los cupos son obligatorios")
    @Min(value = 1, message = "Debe haber al menos 1 cupo")
    private Integer cupos;

    private EstadoOferta estado;

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

}
