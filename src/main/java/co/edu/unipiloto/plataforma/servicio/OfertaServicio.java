package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.OfertaRequest;
import co.edu.unipiloto.plataforma.dto.OfertaResponse;
import co.edu.unipiloto.plataforma.excepcion.ConflictoException;
import co.edu.unipiloto.plataforma.excepcion.ReglaNegocioException;
import co.edu.unipiloto.plataforma.modelo.Curso;
import co.edu.unipiloto.plataforma.modelo.EstadoOferta;
import co.edu.unipiloto.plataforma.modelo.OfertaEducativa;
import co.edu.unipiloto.plataforma.repositorio.CursoRepositorio;
import co.edu.unipiloto.plataforma.repositorio.MatriculaRepositorio;
import co.edu.unipiloto.plataforma.repositorio.OfertaEducativaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/** CRUD de la oferta de educacion virtual. */
@Service
public class OfertaServicio {

    private final OfertaEducativaRepositorio ofertas;
    private final CursoRepositorio cursos;
    private final MatriculaRepositorio matriculas;
    private final Buscador buscar;

    public OfertaServicio(OfertaEducativaRepositorio ofertas, CursoRepositorio cursos,
                          MatriculaRepositorio matriculas, Buscador buscar) {
        this.ofertas = ofertas;
        this.cursos = cursos;
        this.matriculas = matriculas;
        this.buscar = buscar;
    }

    @Transactional(readOnly = true)
    public List<OfertaResponse> listar(EstadoOferta estado) {
        List<OfertaEducativa> base = estado == null ? ofertas.findAll() : ofertas.findByEstado(estado);
        List<OfertaResponse> salida = new ArrayList<>();
        for (OfertaEducativa o : base) {
            salida.add(Conversor.aOferta(o, contarMatriculas(o.getId())));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public OfertaResponse obtener(Long id) {
        OfertaEducativa o = buscar.oferta(id);
        return Conversor.aOferta(o, contarMatriculas(id));
    }

    @Transactional
    public OfertaResponse crear(OfertaRequest req) {
        validarFechas(req);
        if (ofertas.existsByNombreIgnoreCase(req.getNombre().trim())) {
            throw new ConflictoException("Ya existe una oferta llamada " + req.getNombre().trim());
        }
        OfertaEducativa o = new OfertaEducativa();
        copiar(req, o);
        o = ofertas.save(o);
        return Conversor.aOferta(o, 0);
    }

    @Transactional
    public OfertaResponse actualizar(Long id, OfertaRequest req) {
        validarFechas(req);
        OfertaEducativa o = buscar.oferta(id);
        if (!o.getNombre().equalsIgnoreCase(req.getNombre().trim())
                && ofertas.existsByNombreIgnoreCase(req.getNombre().trim())) {
            throw new ConflictoException("Ya existe una oferta llamada " + req.getNombre().trim());
        }
        copiar(req, o);
        o = ofertas.save(o);
        return Conversor.aOferta(o, contarMatriculas(id));
    }

    @Transactional
    public void eliminar(Long id) {
        OfertaEducativa o = buscar.oferta(id);
        if (cursos.existsByOferta_Id(id)) {
            throw new ConflictoException("No se puede eliminar: la oferta tiene cursos asociados. "
                    + "Puede cerrarla con estado=CERRADA");
        }
        ofertas.delete(o);
    }

    private long contarMatriculas(Long ofertaId) {
        long total = 0;
        for (Curso c : cursos.findByOferta_Id(ofertaId)) {
            total += matriculas.findByCurso_Id(c.getId()).size();
        }
        return total;
    }

    private void validarFechas(OfertaRequest req) {
        if (req.getFechaFin().isBefore(req.getFechaInicio())) {
            throw new ReglaNegocioException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    private void copiar(OfertaRequest req, OfertaEducativa o) {
        o.setNombre(req.getNombre().trim());
        o.setDescripcion(req.getDescripcion());
        o.setModalidad(req.getModalidad() == null || req.getModalidad().trim().isEmpty()
                ? "VIRTUAL" : req.getModalidad().trim().toUpperCase());
        o.setFechaInicio(req.getFechaInicio());
        o.setFechaFin(req.getFechaFin());
        o.setCupos(req.getCupos());
        o.setEstado(req.getEstado() == null ? EstadoOferta.ABIERTA : req.getEstado());
    }
}
