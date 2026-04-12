package es.unican.dae.repositorios;

import es.unican.dae.dominio.CategoriaSerie;
import es.unican.dae.dominio.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {

    /** Búsqueda exacta por título (insensible a mayúsculas). */
    Optional<Serie> findByTituloIgnoreCase(String titulo);

    /** Todas las series de una categoría dada, ordenadas alfabéticamente. */
    List<Serie> findByCategoriaOrderByTituloAsc(CategoriaSerie categoria);

    /** Búsqueda parcial por título (útil para un buscador en la UI). */
    List<Serie> findByTituloContainingIgnoreCaseOrderByTituloAsc(String fragmento);
}
