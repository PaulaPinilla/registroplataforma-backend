package co.edu.unipiloto.plataforma.modelo;

import javax.persistence.*;

/**
 * Paso de un proceso formativo. Una actividad pertenece a un solo proceso.
 */
@Entity
@Table(name = "pasos_proceso", uniqueConstraints = @UniqueConstraint(columnNames = {"actividad_id"}))
public class PasoProceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proceso_id", nullable = false)
    private ProcesoFormativo proceso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    @Column(nullable = false)
    private Integer orden;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProcesoFormativo getProceso() { return proceso; }
    public void setProceso(ProcesoFormativo proceso) { this.proceso = proceso; }

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

}
