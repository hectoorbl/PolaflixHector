package es.unican.bringas.Polaflix.repositorios;

import es.unican.bringas.Polaflix.dominio.TipoTarifa;
import es.unican.bringas.Polaflix.dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByNombreUsuarioAndContrasena(String nombreUsuario, String contrasena);

    boolean existsByNombreUsuario(String nombreUsuario);

    // Página de inicio: carga series con su Serie asociada y el estado (capitulosVistos NO
    // se necesitan en la página de inicio, sólo el EstadoSerie que es una columna simple)
    @Query("""
        SELECT DISTINCT u FROM Usuario u
        LEFT JOIN FETCH u.series us
        LEFT JOIN FETCH us.serie
        WHERE u.nombreUsuario = :nombreUsuario
        """)
    Optional<Usuario> findByNombreUsuarioWithSeries(@Param("nombreUsuario") String nombreUsuario);

    // Ver Serie: necesita además los capitulosVistos para saber cuáles están vistos
    @Query("""
        SELECT DISTINCT u FROM Usuario u
        LEFT JOIN FETCH u.series us
        LEFT JOIN FETCH us.serie
        LEFT JOIN FETCH us.capitulosVistos
        WHERE u.nombreUsuario = :nombreUsuario
        """)
    Optional<Usuario> findByNombreUsuarioWithSeriesAndCapitulosVistos(@Param("nombreUsuario") String nombreUsuario);

    // Ver Facturas: carga sólo las facturas (sin líneas, éstas se cargan aparte si se necesitan)
    @Query("""
        SELECT DISTINCT u FROM Usuario u
        LEFT JOIN FETCH u.facturas
        WHERE u.nombreUsuario = :nombreUsuario
        """)
    Optional<Usuario> findByNombreUsuarioWithFacturas(@Param("nombreUsuario") String nombreUsuario);

    List<Usuario> findByTarifa(TipoTarifa tarifa);
}
