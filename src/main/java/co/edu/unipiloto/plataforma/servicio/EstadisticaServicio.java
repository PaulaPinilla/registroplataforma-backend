package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.*;
import co.edu.unipiloto.plataforma.modelo.*;
import co.edu.unipiloto.plataforma.repositorio.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Estadisticas de un curso y generales de la plataforma. */
@Service
public class EstadisticaServicio {

    private static final int TOP = 5;

    private final UsuarioRepositorio usuarios;
    private final RolRepositorio roles;
    private final OfertaEducativaRepositorio ofertas;
    private final CursoRepositorio cursos;
    private final UnidadRepositorio unidades;
    private final ActividadRepositorio actividades;
    private final MatriculaRepositorio matriculas;
    private final EntregaRepositorio entregas;
    private final EvaluacionCursoRepositorio evaluaciones;
    private final Buscador buscar;

    public EstadisticaServicio(UsuarioRepositorio usuarios, RolRepositorio roles, OfertaEducativaRepositorio ofertas,
                               CursoRepositorio cursos, UnidadRepositorio unidades, ActividadRepositorio actividades,
                               MatriculaRepositorio matriculas, EntregaRepositorio entregas,
                               EvaluacionCursoRepositorio evaluaciones, Buscador buscar) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.ofertas = ofertas;
        this.cursos = cursos;
        this.unidades = unidades;
        this.actividades = actividades;
        this.matriculas = matriculas;
        this.entregas = entregas;
        this.evaluaciones = evaluaciones;
        this.buscar = buscar;
    }

    @Transactional(readOnly = true)
    public EstadisticasCursoResponse deCurso(Long cursoId) {
        Curso curso = buscar.curso(cursoId);
        List<Matricula> inscritos = matriculas.findByCurso_Id(cursoId);
        List<Actividad> acts = actividades.findByUnidad_Curso_Id(cursoId);
        List<Entrega> todasEntregas = entregas.findByActividad_Unidad_Curso_Id(cursoId);

        int calificadas = 0;
        List<Double> notasEntregas = new ArrayList<>();
        for (Entrega e : todasEntregas) {
            if (e.estaCalificada()) {
                calificadas++;
                notasEntregas.add(e.getCalificacion());
            }
        }

        int aprobados = 0;
        int reprobados = 0;
        int enCurso = 0;
        double sumaAvance = 0;
        List<Double> notasFinales = new ArrayList<>();
        List<PuestoRanking> ranking = new ArrayList<>();
        Map<String, Integer> distribucion = new LinkedHashMap<>();
        String[] rangos = {"0.0-0.99", "1.0-1.99", "2.0-2.99", "3.0-3.99", "4.0-5.0"};
        for (String r : rangos) {
            distribucion.put(r, 0);
        }

        for (Matricula m : inscritos) {
            Usuario est = m.getEstudiante();
            List<Entrega> propias = new ArrayList<>();
            for (Entrega e : todasEntregas) {
                if (e.getEstudiante().getId().equals(est.getId())) {
                    propias.add(e);
                }
            }
            ProgresoCursoResponse p = ProgresoServicio.construir(curso, est, acts, propias);
            sumaAvance += p.getPorcentajeAvance();

            PuestoRanking pr = new PuestoRanking();
            pr.setEstudianteId(est.getId());
            pr.setNombre(est.getNombre());
            pr.setNotaAcumulada(p.getNotaAcumulada());
            ranking.add(pr);

            if ("APROBADO".equals(p.getEstado())) {
                aprobados++;
            } else if ("REPROBADO".equals(p.getEstado())) {
                reprobados++;
            } else {
                enCurso++;
            }
            if (p.getNotaFinal() != null) {
                notasFinales.add(p.getNotaFinal());
                double n = p.getNotaFinal();
                int idx = n >= 4.0 ? 4 : (int) Math.floor(n);
                distribucion.put(rangos[idx], distribucion.get(rangos[idx]) + 1);
            }
        }

        Collections.sort(ranking, new Comparator<PuestoRanking>() {
            @Override
            public int compare(PuestoRanking a, PuestoRanking b) {
                return Double.compare(b.getNotaAcumulada(), a.getNotaAcumulada());
            }
        });
        if (ranking.size() > TOP) {
            ranking = new ArrayList<>(ranking.subList(0, TOP));
        }

        List<Double> puntajes = new ArrayList<>();
        for (EvaluacionCurso ev : evaluaciones.findByCurso_Id(cursoId)) {
            puntajes.add((double) ev.getPuntaje());
        }

        int estudiantes = inscritos.size();
        int esperadas = estudiantes * acts.size();

        EstadisticasCursoResponse r = new EstadisticasCursoResponse();
        r.setCursoId(curso.getId());
        r.setCursoTitulo(curso.getTitulo());
        r.setEstudiantesMatriculados(estudiantes);
        r.setTotalActividades(acts.size());
        r.setEntregasRecibidas(todasEntregas.size());
        r.setEntregasCalificadas(calificadas);
        r.setEntregasPorCalificar(todasEntregas.size() - calificadas);
        r.setTasaEntregaPorcentaje(esperadas == 0 ? 0 : Numeros.redondear(todasEntregas.size() * 100.0 / esperadas));
        r.setPromedioNotasEntregas(Numeros.promedio(notasEntregas));
        r.setPromedioAvancePorcentaje(estudiantes == 0 ? 0 : Numeros.redondear(sumaAvance / estudiantes));
        r.setAprobados(aprobados);
        r.setReprobados(reprobados);
        r.setEnCurso(enCurso);
        r.setPromedioNotaFinal(Numeros.promedio(notasFinales));
        r.setEvaluacionPromedioCurso(Numeros.promedio(puntajes));
        r.setNumeroEvaluaciones(puntajes.size());
        r.setDistribucionNotaFinal(distribucion);
        r.setMejoresEstudiantes(ranking);
        return r;
    }

    @Transactional(readOnly = true)
    public EstadisticasGeneralResponse general() {
        EstadisticasGeneralResponse r = new EstadisticasGeneralResponse();
        r.setTotalUsuarios(usuarios.count());
        r.setTotalDocentes(contarPorRol(Roles.DOCENTE));
        r.setTotalEstudiantes(contarPorRol(Roles.ESTUDIANTE));
        r.setTotalOfertas(ofertas.count());
        r.setTotalCursos(cursos.count());
        r.setTotalUnidades(unidades.count());
        r.setTotalActividades(actividades.count());
        r.setTotalMatriculas(matriculas.count());
        r.setTotalEntregas(entregas.count());

        long calificadas = 0;
        List<Double> notas = new ArrayList<>();
        for (Entrega e : entregas.findAll()) {
            if (e.estaCalificada()) {
                calificadas++;
                notas.add(e.getCalificacion());
            }
        }
        r.setEntregasCalificadas(calificadas);
        r.setPromedioNotasGeneral(Numeros.promedio(notas));

        List<Double> puntajes = new ArrayList<>();
        for (EvaluacionCurso ev : evaluaciones.findAll()) {
            puntajes.add((double) ev.getPuntaje());
        }
        r.setTotalEvaluaciones(puntajes.size());
        r.setPromedioEvaluacionCursos(Numeros.promedio(puntajes));

        List<CursoPopular> populares = new ArrayList<>();
        for (Curso c : cursos.findAll()) {
            CursoPopular cp = new CursoPopular();
            cp.setCursoId(c.getId());
            cp.setTitulo(c.getTitulo());
            cp.setMatriculados(matriculas.findByCurso_Id(c.getId()).size());
            populares.add(cp);
        }
        Collections.sort(populares, new Comparator<CursoPopular>() {
            @Override
            public int compare(CursoPopular a, CursoPopular b) {
                return Long.compare(b.getMatriculados(), a.getMatriculados());
            }
        });
        if (populares.size() > TOP) {
            populares = new ArrayList<>(populares.subList(0, TOP));
        }
        r.setCursosMasMatriculados(populares);
        return r;
    }

    private long contarPorRol(String nombreRol) {
        return roles.findByNombreIgnoreCase(nombreRol)
                .map(rol -> (long) usuarios.findByRol_Id(rol.getId()).size())
                .orElse(0L);
    }
}
