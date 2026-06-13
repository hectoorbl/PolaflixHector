package es.unican.bringas.Polaflix.repositorios;

import es.unican.bringas.Polaflix.dominio.Factura;
import es.unican.bringas.Polaflix.dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

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

    List<Factura> findByUsuarioOrderByAnioDescMesDesc(Usuario usuario);
}
