package es.unican.bringas.Polaflix.repositorios;

import es.unican.bringas.Polaflix.dominio.Factura;
import es.unican.bringas.Polaflix.dominio.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByUsuarioAndMesAndAnio(Usuario usuario, int mes, int anio);

    @Query("""
        SELECT f FROM Factura f
        LEFT JOIN FETCH f.lineas
        WHERE f.usuario = :usuario
          AND f.mes     = :mes
          AND f.anio    = :anio
        """)
    Optional<Factura> findByUsuarioAndMesAndAnioWithLineas(
        @Param("usuario") Usuario usuario,
        @Param("mes")     int mes,
        @Param("anio")    int anio
    );

    boolean existsByUsuarioAndMesAndAnio(Usuario usuario, int mes, int anio);

    List<Factura> findByUsuarioOrderByAnioDescMesDesc(Usuario usuario);

    @Query("""
        SELECT f FROM Factura f
        WHERE f.usuario = :usuario
        ORDER BY f.anio DESC, f.mes DESC
        """)
    List<Factura> findMasRecienteByUsuario(@Param("usuario") Usuario usuario, Pageable pageable);

    List<Factura> findByUsuarioAndAnioOrderByMesAsc(Usuario usuario, int anio);
}
