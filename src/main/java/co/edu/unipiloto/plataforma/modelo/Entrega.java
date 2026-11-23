package co.edu.unipiloto.plataforma.modelo;

import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * Entrega de un estudiante para una actividad, con su calificacion y retroalimentacion.
 */
@Entity
@Table(name = "entregas", uniqueConstraints = @UniqueConstraint(columnNames = {"actividad_id", "estudiante_id"}))
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @Column(length = 4000)
    private String textoRespuesta;

    @Column(length = 255)
    private String nombreArchivo;

    @Column(length = 255)
    private String rutaArchivo;

    private LocalDateTime fechaEntrega;

    private Double calificacion;

    @Column(length = 2000)
    private String retroalimentacion;

    private LocalDateTime fechaCalificacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }

    public Usuario getEstudiante() { return estudiante; }
    public void setEstudiante(Usuario estudiante) { this.estudiante = estudiante; }

    public String getTextoRespuesta() { return textoRespuesta; }
    public void setTextoRespuesta(String textoRespuesta) { this.textoRespuesta = textoRespuesta; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }

    public String getRetroalimentacion() { return retroalimentacion; }
    public void setRetroalimentacion(String retroalimentacion) { this.retroalimentacion = retroalimentacion; }

    public LocalDateTime getFechaCalificacion() { return fechaCalificacion; }
    public void setFechaCalificacion(LocalDateTime fechaCalificacion) { this.fechaCalificacion = fechaCalificacion; }

    /** true cuando el docente ya registro una nota. */
    public boolean estaCalificada() {
        return calificacion != null;
    }

}
