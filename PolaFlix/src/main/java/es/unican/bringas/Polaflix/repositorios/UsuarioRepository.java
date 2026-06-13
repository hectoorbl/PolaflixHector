package es.unican.bringas.Polaflix.repositorios;

import es.unican.bringas.Polaflix.dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByNombreUsuarioAndContrasena(String nombreUsuario, String contrasena);

    boolean existsByNombreUsuario(String nombreUsuario);

    @Query("""
        SELECT DISTINCT u FROM Usuario u
        LEFT JOIN FETCH u.series us
        LEFT JOIN FETCH us.serie
        WHERE u.nombreUsuario = :nombreUsuario
        """)
    Optional<Usuario> findByNombreUsuarioWithSeries(@Param("nombreUsuario") String nombreUsuario);

    @Query("""
        SELECT DISTINCT u FROM Usuario u
        LEFT JOIN FETCH u.series us
        LEFT JOIN FETCH us.serie
        LEFT JOIN FETCH us.capitulosVistos
        WHERE u.nombreUsuario = :nombreUsuario
        """)
    Optional<Usuario> findByNombreUsuarioWithSeriesAndCapitulosVistos(@Param("nombreUsuario") String nombreUsuario);
}
