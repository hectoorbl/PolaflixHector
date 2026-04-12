package es.unican.dae.repositorios;

import es.unican.dae.dominio.EstadoSerie;
import es.unican.dae.dominio.Serie;
import es.unican.dae.dominio.Usuario;
import es.unican.bringas.Polaflix.repositorio.dominio.UsuarioSerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioSerieRepository extends JpaRepository<UsuarioSerie, Long> {

    /** Todos los seguimientos de un usuario, ordenados por título de serie. */
    List<UsuarioSerie> findByUsuarioOrderBySerieAsc(Usuario usuario);

    /** Seguimientos de un usuario filtrados por estado (PENDIENTE, EMPEZADA, TERMINADA). */
    List<UsuarioSerie> findByUsuarioAndEstadoOrderBySerieAsc(Usuario usuario, EstadoSerie estado);

    /** Comprueba si un usuario ya sigue una serie concreta. */
    Optional<UsuarioSerie> findByUsuarioAndSerie(Usuario usuario, Serie serie);
}
