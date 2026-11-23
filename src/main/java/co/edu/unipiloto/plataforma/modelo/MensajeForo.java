package co.edu.unipiloto.plataforma.modelo;

import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * Mensaje dentro de un foro o chat.
 */
@Entity
@Table(name = "mensajes_foro")
public class MensajeForo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "foro_id", nullable = false)
    private Foro foro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(nullable = false, length = 2000)
    private String contenido;

    private Long respuestaAId;

    private LocalDateTime fecha;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Foro getForo() { return foro; }
    public void setForo(Foro foro) { this.foro = foro; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Long getRespuestaAId() { return respuestaAId; }
    public void setRespuestaAId(Long respuestaAId) { this.respuestaAId = respuestaAId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

}
