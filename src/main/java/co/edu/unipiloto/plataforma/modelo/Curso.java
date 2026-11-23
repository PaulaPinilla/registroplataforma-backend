package co.edu.unipiloto.plataforma.modelo;

import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * Curso virtual creado por un docente. Cada corte tiene un peso en la nota final (suman 100).
 */
@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String semestre;

    @Column(nullable = false)
    private LocalDateTime fechaFinalizacion;

    private double pesoCorte1;

    private double pesoCorte2;

    private double pesoCorte3;

    @Column(length = 100)
    private String claveMatricula;

    @ManyToOne(optional = false)
    @JoinColumn(name = "docente_id", nullable = false)
    private Usuario docente;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private OfertaEducativa oferta;

    @Column(length = 2000)
    private String objetivos;

    @Column(length = 2000)
    private String resultadosAprendizaje;

    private Integer cupos;

    private LocalDateTime fechaInicio;

    private Boolean publicado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

    public double getPesoCorte1() { return pesoCorte1; }
    public void setPesoCorte1(double pesoCorte1) { this.pesoCorte1 = pesoCorte1; }

    public double getPesoCorte2() { return pesoCorte2; }
    public void setPesoCorte2(double pesoCorte2) { this.pesoCorte2 = pesoCorte2; }

    public double getPesoCorte3() { return pesoCorte3; }
    public void setPesoCorte3(double pesoCorte3) { this.pesoCorte3 = pesoCorte3; }

    public String getClaveMatricula() { return claveMatricula; }
    public void setClaveMatricula(String claveMatricula) { this.claveMatricula = claveMatricula; }

    public Usuario getDocente() { return docente; }
    public void setDocente(Usuario docente) { this.docente = docente; }

    public OfertaEducativa getOferta() { return oferta; }
    public void setOferta(OfertaEducativa oferta) { this.oferta = oferta; }

    public String getObjetivos() { return objetivos; }
    public void setObjetivos(String objetivos) { this.objetivos = objetivos; }

    public String getResultadosAprendizaje() { return resultadosAprendizaje; }
    public void setResultadosAprendizaje(String resultadosAprendizaje) { this.resultadosAprendizaje = resultadosAprendizaje; }

    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public Boolean getPublicado() { return publicado; }
    public void setPublicado(Boolean publicado) { this.publicado = publicado; }

    /** Peso (en porcentaje) del corte 1, 2 o 3 en la nota final. */
    public double pesoDelCorte(int corte) {
        switch (corte) {
            case 1: return pesoCorte1;
            case 2: return pesoCorte2;
            case 3: return pesoCorte3;
            default: return 0;
        }
    }

    /** true si los pesos de los tres cortes suman 100. */
    public boolean tienePesosValidos() {
        return Math.abs((pesoCorte1 + pesoCorte2 + pesoCorte3) - 100.0) < 0.001;
    }

    /** true cuando ya paso la fecha de finalizacion del curso. */
    public boolean yaTermino() {
        return fechaFinalizacion != null && LocalDateTime.now().isAfter(fechaFinalizacion);
    }

    /** true cuando el coordinador ya publico el curso. */
    public boolean estaPublicado() {
        return Boolean.TRUE.equals(publicado);
    }

    /** true si el curso exige clave para matricularse. */
    public boolean exigeClave() {
        return claveMatricula != null && !claveMatricula.trim().isEmpty();
    }

}
