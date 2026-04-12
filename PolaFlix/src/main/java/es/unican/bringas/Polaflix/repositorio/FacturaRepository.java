package es.unican.dae.repositorios;

import es.unican.dae.dominio.Factura;
import es.unican.dae.dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    /**
     * Busca la factura de un mes y año concretos para un usuario.
     * Equivale al método getFacturaDelMes de la lógica de dominio.
     */
    Optional<Factura> findByUsuarioAndMesAndAño(Usuario usuario, int mes, int año);

    /** Historial completo de facturas de un usuario, del más reciente al más antiguo. */
    List<Factura> findByUsuarioOrderByAñoDescMesDesc(Usuario usuario);
}
