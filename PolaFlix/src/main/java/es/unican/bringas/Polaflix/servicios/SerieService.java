package es.unican.bringas.Polaflix.servicios;

import es.unican.bringas.Polaflix.dominio.Serie;
import es.unican.bringas.Polaflix.dominio.dto.SerieDTO;
import es.unican.bringas.Polaflix.repositorios.SerieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class SerieService {

    private final SerieRepository serieRepo;

    public SerieService(SerieRepository serieRepo) { this.serieRepo = serieRepo; }

    /** Catálogo. Filtros opcionales y excluyentes. 400 si llegan ambos, 404 si no hay resultados. */
    public ResponseEntity<List<SerieDTO>> listar(String inicial, String query) {
        if (inicial != null && query != null)
            return ResponseEntity.badRequest().build();

        List<Serie> series;

        if (inicial != null && !inicial.isBlank()) {
            series = serieRepo.findByTituloStartingWithIgnoreCaseOrderByTituloAsc(inicial.trim());
        } else if (query != null && !query.isBlank()) {
            String q = query.trim().toLowerCase();
            series = serieRepo.findAll().stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(q))
                .sorted(Comparator.comparing(Serie::getTitulo, String.CASE_INSENSITIVE_ORDER))
                .toList();
        } else {
            series = serieRepo.findAll().stream()
                .sorted(Comparator.comparing(Serie::getTitulo, String.CASE_INSENSITIVE_ORDER))
                .toList();
        }

        if (series.isEmpty() && (inicial != null || query != null))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(series.stream().map(SerieDTO::resumen).toList());
    }

    /** Detalle de una serie. 404 si no existe. */
    public ResponseEntity<SerieDTO> detalle(String titulo) {
        Optional<Serie> opt = serieRepo.findByTituloIgnoreCase(titulo);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Serie serie = opt.get();
        // Cargamos las colecciones perezosas dentro de la transacción.
        serie.getActores().size();
        serie.getCreadores().size();
        serie.getTemporadas().values().forEach(t -> t.getCapitulos().size());
        return ResponseEntity.ok(SerieDTO.detalle(serie));
    }

    /** Para uso interno desde otros servicios. */
    public Optional<Serie> buscarPorTitulo(String titulo) {
        return serieRepo.findByTituloIgnoreCase(titulo);
    }
}
