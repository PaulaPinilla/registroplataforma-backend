package co.edu.unipiloto.plataforma.servicio;

import co.edu.unipiloto.plataforma.dto.UnidadRequest;
import co.edu.unipiloto.plataforma.dto.UnidadResponse;
import co.edu.unipiloto.plataforma.modelo.Curso;
import co.edu.unipiloto.plataforma.modelo.Unidad;
import co.edu.unipiloto.plataforma.repositorio.UnidadRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/** CRUD de unidades tematicas de un curso. */
@Service
public class UnidadServicio {

    private final UnidadRepositorio unidades;
    private final Buscador buscar;
    private final Limpieza limpieza;

    public UnidadServicio(UnidadRepositorio unidades, Buscador buscar, Limpieza limpieza) {
        this.unidades = unidades;
        this.buscar = buscar;
        this.limpieza = limpieza;
    }

    @Transactional(readOnly = true)
    public List<UnidadResponse> listar(Long cursoId) {
        List<Unidad> base;
        if (cursoId != null) {
            buscar.curso(cursoId);
            base = unidades.findByCurso_IdOrderByOrdenAsc(cursoId);
        } else {
            base = unidades.findAll();
        }
        List<UnidadResponse> salida = new ArrayList<>();
        for (Unidad u : base) {
            salida.add(Conversor.aUnidad(u));
        }
        return salida;
    }

    @Transactional(readOnly = true)
    public UnidadResponse obtener(Long id) {
        return Conversor.aUnidad(buscar.unidad(id));
    }

    @Transactional
    public UnidadResponse crear(UnidadRequest req) {
        Curso curso = buscar.curso(req.getCursoId());
        Unidad u = new Unidad();
        u.setTitulo(req.getTitulo().trim());
        u.setCurso(curso);
        u.setOrden(req.getOrden() != null ? req.getOrden()
                : unidades.findByCurso_IdOrderByOrdenAsc(curso.getId()).size() + 1);
        return Conversor.aUnidad(unidades.save(u));
    }

    @Transactional
    public UnidadResponse actualizar(Long id, UnidadRequest req) {
        Unidad u = buscar.unidad(id);
        u.setTitulo(req.getTitulo().trim());
        if (req.getOrden() != null) {
            u.setOrden(req.getOrden());
        }
        u.setCurso(buscar.curso(req.getCursoId()));
        return Conversor.aUnidad(unidades.save(u));
    }

    @Transactional
    public void eliminar(Long id) {
        buscar.unidad(id);
        limpieza.borrarUnidad(id);
    }
}
