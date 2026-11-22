package co.edu.unipiloto.plataforma.dto;

import javax.validation.constraints.*;

public class RolRequest {

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre del rol admite maximo 50 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripcion admite maximo 255 caracteres")
    private String descripcion;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

}
