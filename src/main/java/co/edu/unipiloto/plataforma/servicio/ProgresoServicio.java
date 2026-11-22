package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.ActividadProgreso;
import co.edu.unipiloto.plataforma.dto.CorteProgreso;
import co.edu.unipiloto.plataforma.dto.ProgresoCursoResponse;
import co.edu.unipiloto.plataforma.excepcion.RecursoNoEncontradoException;
import co.edu.unipiloto.plataforma.modelo.Actividad;
import co.edu.unipiloto.plataforma.modelo.Curso;
import co.edu.unipiloto.plataforma.modelo.Entrega;
import co.edu.unipiloto.plataforma.modelo.Matricula;
import co.edu.unipiloto.plataforma.modelo.Usuario;
import co.edu.unipiloto.plataforma.repositorio.ActividadRepositorio;
import co.edu.unipiloto.plataforma.repositorio.EntregaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Seguimiento de progreso. Replica la logica de notas de la app Android:
 *  - PONDERADO: cada actividad pesa su porcentaje dentro del corte y cada corte pesa lo definido en el curso
 *    (los pesos de los cortes deben sumar 100).
 *  - PROMEDIO_SIMPLE: si los pesos no suman 100, la nota es el promedio de todas las actividades.
 *  - Una actividad sin calificar cuenta como 0 en la nota acumulada.
 *  - La nota final solo existe cuando TODAS las actividades estan calificadas; se aprueba con 3.0 o mas.
 */
@Service
public class ProgresoServicio {

    public static final double NOTA_APROBACION = 3.0;

    private final MatriculaRepositorio matriculas;
    private final ActividadRepositorio actividades;
    private final EntregaRepositorio entregas;
    private final Buscador buscar;

    public ProgresoServicio(MatriculaRepositorio matriculas, ActividadRepositorio actividades,
                            EntregaRepositorio entregas, Buscador buscar) {
        this.matriculas = matriculas;
        this.actividades = actividades;
        this.entregas = entregas;
        this.buscar = buscar;
    }

    @Transactional(readOnly = true)
    public ProgresoCursoResponse deEstudiante(Long cursoId, Long estudianteId) {
        Curso curso = buscar.curso(cursoId);
        Usuario est = buscar.usuario(estudianteId);
        if (!matriculas.existsByEstudiante_IdAndCurso_Id(estudianteId, cursoId)) {
            throw new RecursoNoEncontradoException("El estudiante " + estudianteId
                    + " no esta matriculado en el curso " + cursoId);
        }
        return construir(curso, est, actividades.findByUnidad_Curso_Id(cursoId),
                entregas.findByActividad_Unidad_Curso_IdAndEstudiante_Id(cursoId, estudianteId));
    }

    @Transactional(readOnly = true)
    public List<ProgresoCursoResponse> deCurso(Long cursoId) {
        Curso curso = buscar.curso(cursoId);
        List<ProgresoCursoResponse> salida = new ArrayList<>();
        for (Matricula m : matriculas.findByCurso_Id(cursoId)) {
            salida.add(construir(curso, m.getEstudiante(), actividades.findByUnidad_Curso_Id(cursoId),
                    entregas.findByActividad_Unidad_Curso_IdAndEstudiante_Id(cursoId, m.getEstudiante().getId())));
        }
        return salida;
    }

    /** Calculo puro (sin base de datos) para poder probarlo aislado. */
    public static ProgresoCursoResponse construir(Curso curso, Usuario est, List<Actividad> acts, List<Entrega> ents) {
        boolean ponderado = curso.tienePesosValidos();

        Map<Long, Entrega> porActividad = new HashMap<>();
        for (Entrega e : ents) {
            porActividad.put(e.getActividad().getId(), e);
        }

        int entregadas = 0;
        int calificadas = 0;
        double sumaTodas = 0;
        double acumulada = 0;
        List<CorteProgreso> cortes = new ArrayList<>();

        for (int corte = 1; corte <= 3; corte++) {
            double porcentajeAsignado = 0;
            double sumaPonderadaCorte = 0;
            double sumaSimpleCorte = 0;
            int cantidadCorte = 0;
            List<ActividadProgreso> detalle = new ArrayList<>();

            for (Actividad a : acts) {
                if (a.getCorte() != corte) {
                    continue;
                }
                Entrega e = porActividad.get(a.getId());
                Double nota = null;
                String estado = "SIN_ENTREGAR";
                if (e != null) {
                    entregadas++;
                    estado = "ENTREGADA";
                    if (e.estaCalificada()) {
                        calificadas++;
                        estado = "CALIFICADA";
                        nota = e.getCalificacion();
                    }
                }
                double valor = nota == null ? 0 : nota;
                porcentajeAsignado += a.getPorcentaje();
                sumaPonderadaCorte += valor * a.getPorcentaje() / 100.0;
                sumaSimpleCorte += valor;
                sumaTodas += valor;
                cantidadCorte++;

                ActividadProgreso ap = new ActividadProgreso();
                ap.setActividadId(a.getId());
                ap.setTitulo(a.getTitulo());
                ap.setCategoria(a.getCategoria());
                ap.setPorcentaje(a.getPorcentaje());
                ap.setFechaLimite(a.getFechaLimite());
                ap.setEstado(estado);
                ap.setNota(nota);
                detalle.add(ap);
            }

            double notaCorte = ponderado ? sumaPonderadaCorte : (cantidadCorte == 0 ? 0 : sumaSimpleCorte / cantidadCorte);
            if (ponderado) {
                acumulada += notaCorte * curso.pesoDelCorte(corte) / 100.0;
            }

            CorteProgreso cp = new CorteProgreso();
            cp.setCorte(corte);
            cp.setPesoCorte(curso.pesoDelCorte(corte));
            cp.setPorcentajeAsignado(Numeros.redondear(porcentajeAsignado));
            cp.setNotaCorte(Numeros.redondear(notaCorte));
            cp.setActividades(detalle);
            cortes.add(cp);
        }

        int total = acts.size();
        if (!ponderado) {
            acumulada = total == 0 ? 0 : sumaTodas / total;
        }

        ProgresoCursoResponse r = new ProgresoCursoResponse();
        r.setCursoId(curso.getId());
        r.setCursoTitulo(curso.getTitulo());
        r.setEstudianteId(est.getId());
        r.setEstudianteNombre(est.getNombre());
        r.setCursoFinalizado(curso.yaTermino());
        r.setModoCalculo(ponderado ? "PONDERADO" : "PROMEDIO_SIMPLE");
        r.setTotalActividades(total);
        r.setEntregadas(entregadas);
        r.setCalificadas(calificadas);
        r.setPendientesPorEntregar(total - entregadas);
        r.setPorcentajeAvance(total == 0 ? 0 : Numeros.redondear(entregadas * 100.0 / total));
        r.setNotaAcumulada(Numeros.redondear(acumulada));

        Double notaFinal = (total > 0 && calificadas == total) ? Numeros.redondear(acumulada) : null;
        r.setNotaFinal(notaFinal);
        if (notaFinal == null) {
            r.setEstado("EN_CURSO");
        } else {
            r.setEstado(notaFinal >= NOTA_APROBACION ? "APROBADO" : "REPROBADO");
        }
        r.setCortes(cortes);
        return r;
    }
}
