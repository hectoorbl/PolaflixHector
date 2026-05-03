package es.unican.bringas.Polaflix.repositorios;

import es.unican.bringas.Polaflix.dominio.CategoriaSerie;
import es.unican.bringas.Polaflix.dominio.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloIgnoreCase(String titulo);

    boolean existsByTituloIgnoreCase(String titulo);

    List<Serie> findByTituloStartingWithIgnoreCaseOrderByTituloAsc(String inicial);

    @Query("""
        SELECT s FROM Serie s
        LEFT JOIN FETCH s.actores
        LEFT JOIN FETCH s.creadores
        WHERE s.titulo = :titulo
        """)
    Optional<Serie> findByTituloWithPersonas(@Param("titulo") String titulo);

    @Query("""
        SELECT DISTINCT s FROM Serie s
        LEFT JOIN FETCH s.temporadas t
        LEFT JOIN FETCH t.capitulos
        WHERE s.id = :id
        """)
    Optional<Serie> findByIdWithTemporadasYCapitulos(@Param("id") Long id);

    List<Serie> findByCategoriaOrderByTituloAsc(CategoriaSerie categoria);
}
