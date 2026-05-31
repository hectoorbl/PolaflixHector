package es.unican.bringas.Polaflix.servicios;

import es.unican.bringas.Polaflix.dominio.Serie;
import es.unican.bringas.Polaflix.dominio.Usuario;
import es.unican.bringas.Polaflix.dominio.dto.SerieBuscadaDTO;
import es.unican.bringas.Polaflix.dominio.dto.SerieDetalleDTO;
import es.unican.bringas.Polaflix.dominio.dto.SerieResumenDTO;
import es.unican.bringas.Polaflix.excepciones.RecursoNoEncontradoException;
import es.unican.bringas.Polaflix.repositorios.SerieRepository;
import es.unican.bringas.Polaflix.repositorios.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SerieService {

    private final SerieRepository   serieRepo;
    private final UsuarioRepository usuarioRepo;

    public SerieService(SerieRepository serieRepo, UsuarioRepository usuarioRepo) {
        this.serieRepo   = serieRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public List<SerieResumenDTO> listarPorInicial(String nombreUsuario, String inicial) {
        Usuario u = buscarUsuarioConSeries(nombreUsuario);

        List<Serie> series = (inicial != null && !inicial.isBlank())
                ? serieRepo.findByTituloStartingWithIgnoreCaseOrderByTituloAsc(inicial.trim())
                : serieRepo.findAll().stream()
                        .sorted(Comparator.comparing(Serie::getTitulo, String.CASE_INSENSITIVE_ORDER))
                        .toList();

        return series.stream()
                .map(s -> new SerieResumenDTO(s, u.tieneSerie(s)))
                .toList();
    }

    public List<SerieBuscadaDTO> buscarPorNombre(String nombreUsuario, String nombre) {
        Usuario u = buscarUsuarioConSeries(nombreUsuario);

        Serie encontrada = serieRepo.findByTituloIgnoreCase(nombre.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Serie no encontrada: " + nombre));

        List<Serie> lista = serieRepo.findByTituloStartingWithIgnoreCaseOrderByTituloAsc(encontrada.inicial());

        return lista.stream()
                .map(s -> new SerieBuscadaDTO(s, u.tieneSerie(s),
                        s.getTitulo().equalsIgnoreCase(encontrada.getTitulo())))
                .toList();
    }

    public SerieDetalleDTO obtenerDetalle(String titulo, String nombreUsuario) {
        Serie s = serieRepo.findByTituloWithPersonas(titulo)
                .orElseGet(() -> serieRepo.findByTituloIgnoreCase(titulo)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Serie no encontrada: " + titulo)));
        Usuario u = buscarUsuarioConSeries(nombreUsuario);
        s.getActores().size();
        s.getCreadores().size();
        return new SerieDetalleDTO(s, u.tieneSerie(s));
    }

    public Serie buscarSerie(String titulo) {
        return serieRepo.findByTituloIgnoreCase(titulo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Serie no encontrada: " + titulo));
    }

    private Usuario buscarUsuarioConSeries(String nombreUsuario) {
        return usuarioRepo.findByNombreUsuarioWithSeries(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + nombreUsuario));
    }
}
