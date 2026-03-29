package es.unican.dae.dominio;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

/**
 * LineaFactura es un @Embeddable: no tiene identidad propia ni ciclo de vida
 * independiente. Vive dentro del @ElementCollection de Factura y se
 * almacena en la tabla "lineas_factura".
 *
 * Las referencias a Serie y Capitulo se mapean como @ManyToOne dentro
 * del embeddable (soportado por Hibernate).
 */
@Embeddable
@Getter
public class LineaFactura implements Comparable<LineaFactura> {

    /**
     * Referencia a la serie visualizada.
     * Sin cascade: Serie tiene ciclo de vida propio.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "serie_id", nullable = false)
    private Serie serie;

    /**
     * Referencia al capítulo visualizado.
     * Sin cascade: Capitulo tiene ciclo de vida propio gestionado por Serie.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "capitulo_id", nullable = false)
    private Capitulo capitulo;

    @Column(name = "fecha_visualizacion", nullable = false)
    private LocalDate fechaVisualizacion;

    @Column(nullable = false)
    private double cargo;

    // Constructor protegido requerido por JPA/Hibernate para @Embeddable
    protected LineaFactura() {}

    public LineaFactura(Serie serie, Capitulo capitulo, LocalDate fechaVisualizacion) {
        this.serie              = Objects.requireNonNull(serie,              "La serie no puede ser nula");
        this.capitulo           = Objects.requireNonNull(capitulo,           "El capítulo no puede ser nulo");
        this.fechaVisualizacion = Objects.requireNonNull(fechaVisualizacion, "La fecha no puede ser nula");
        this.cargo              = calcularCargo(serie.getCategoria());
    }

    public double calcularCargo(CategoriaSerie categoria) {
        Objects.requireNonNull(categoria, "La categoría no puede ser nula");
        return categoria.getCoste();
    }

    // ── Object overrides ──────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineaFactura lf)) return false;
        return Objects.equals(capitulo, lf.capitulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capitulo);
    }

    @Override
    public int compareTo(LineaFactura other) {
        int cmp = this.fechaVisualizacion.compareTo(other.fechaVisualizacion);
        if (cmp != 0) return cmp;
        cmp = this.serie.compareTo(other.serie);
        return cmp != 0 ? cmp : this.capitulo.compareTo(other.capitulo);
    }

    @Override
    public String toString() {
        return String.format("LineaFactura{serie='%s', capitulo=%s, fecha=%s, cargo=%.2f€}",
                serie.getTitulo(), capitulo, fechaVisualizacion, cargo);
    }
}
