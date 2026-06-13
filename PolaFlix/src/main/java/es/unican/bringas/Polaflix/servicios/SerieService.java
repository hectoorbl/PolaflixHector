package es.unican.bringas.Polaflix.servicios;

import es.unican.bringas.Polaflix.dominio.Serie;
import es.unican.bringas.Polaflix.dominio.dto.SerieBuscadaDTO;
import es.unican.bringas.Polaflix.dominio.dto.SerieDetalleDTO;
import es.unican.bringas.Polaflix.dominio.dto.SerieResumenDTO;
import es.unican.bringas.Polaflix.repositorios.SerieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SerieService {

    private final SerieRepository serieRepo;

    public SerieService(SerieRepository serieRepo) {
        this.serieRepo = serieRepo;
    }

    public List<SerieResumenDTO> listarPorInicial(String inicial) {
        List<Serie> series = (inicial != null && !inicial.isBlank())
                ? serieRepo.findByTituloStartingWithIgnoreCaseOrderByTituloAsc(inicial.trim())
                : serieRepo.findAll().stream()
                        .sorted(Comparator.comparing(Serie::getTitulo, String.CASE_INSENSITIVE_ORDER))
                        .toList();

        return series.stream().map(SerieResumenDTO::new).toList();
    }

    public List<SerieBuscadaDTO> buscarPorNombre(String nombre) {
        Serie encontrada = serieRepo.findByTituloIgnoreCase(nombre.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serie no encontrada: " + nombre));

        List<Serie> lista = serieRepo.findByTituloStartingWithIgnoreCaseOrderByTituloAsc(encontrada.inicial());

        return lista.stream()
                .map(s -> new SerieBuscadaDTO(s, s.getTitulo().equalsIgnoreCase(encontrada.getTitulo())))
                .toList();
    }

    public SerieDetalleDTO obtenerDetalle(String titulo) {
        Serie s = serieRepo.findByTituloWithPersonas(titulo)
                .orElseGet(() -> serieRepo.findByTituloIgnoreCase(titulo)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serie no encontrada: " + titulo)));
        s.getActores().size();
        s.getCreadores().size();
        return new SerieDetalleDTO(s);
    }

    public Serie buscarSerie(String titulo) {
        return serieRepo.findByTituloIgnoreCase(titulo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serie no encontrada: " + titulo));
    }
}
