package es.unican.bringas.Polaflix.repositorio;

import es.unican.dae.dominio.Capitulo;
import es.unican.dae.dominio.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CapituloRepository extends JpaRepository<Capitulo, Long> {

    /** Todos los capítulos de una temporada concreta, ordenados por número. */
    List<Capitulo> findBySerieAndTemporadaOrderByNumeroAsc(Serie serie, int temporada);

    /** Busca un capítulo concreto dado su serie, temporada y número. */
    Optional<Capitulo> findBySerieAndTemporadaAndNumero(Serie serie, int temporada, int numero);
}
