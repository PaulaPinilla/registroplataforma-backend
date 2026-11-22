package co.edu.unipiloto.plataforma.dto;

import co.edu.unipiloto.plataforma.modelo.Categoria;
import co.edu.unipiloto.plataforma.modelo.TipoEntrega;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

public class ActividadRequest {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150, message = "El titulo admite maximo 150 caracteres")
    private String titulo;

    @Size(max = 2000, message = "Las instrucciones admiten maximo 2000 caracteres")
    private String instrucciones;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha limite es obligatoria")
    private LocalDateTime fechaLimite;

    @NotNull(message = "El tipo de entrega es obligatorio (ARCHIVO, TEXTO o PREGUNTAS)")
    private TipoEntrega tipoEntrega;

    @NotNull(message = "La categoria es obligatoria")
    private Categoria categoria;

    @NotNull(message = "El corte es obligatorio")
    @Min(value = 1, message = "El corte debe ser 1, 2 o 3")
    @Max(value = 3, message = "El corte debe ser 1, 2 o 3")
    private Integer corte;

    @NotNull(message = "El porcentaje es obligatorio")
    @DecimalMin(value = "0.01", message = "El porcentaje debe ser mayor a 0")
    @DecimalMax(value = "100", message = "El porcentaje no puede superar 100")
    private Double porcentaje;

    @NotNull(message = "El unidadId es obligatorio")
    private Long unidadId;

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

    public Integer getCorte() { return corte; }
    public void setCorte(Integer corte) { this.corte = corte; }

    public Double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Double porcentaje) { this.porcentaje = porcentaje; }

    public Long getUnidadId() { return unidadId; }
    public void setUnidadId(Long unidadId) { this.unidadId = unidadId; }

}
