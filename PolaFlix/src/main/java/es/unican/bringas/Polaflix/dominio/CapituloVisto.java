package es.unican.dae.dominio;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

/**
 * CapituloVisto es un @Embeddable: no tiene identidad propia ni ciclo de vida
 * independiente. Vive dentro del @ElementCollection de UsuarioSerie y se
 * almacena en la tabla "capitulos_vistos".
 *
 * La referencia a Capitulo se mapea como @ManyToOne dentro del embeddable,
 * lo que está soportado por Hibernate (aunque no es parte del estándar JPA puro).
 */
@Embeddable
@Getter
public class CapituloVisto implements Comparable<CapituloVisto> {

    /**
     * Referencia al capítulo visto.
     * fetch LAZY para no cargar el capítulo (ni su serie) salvo que se necesite.
     * Sin cascade: Capitulo tiene su propio ciclo de vida gestionado por Serie.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "capitulo_id", nullable = false)
    private Capitulo capitulo;

    @Column(name = "fecha_visualizacion", nullable = false)
    private LocalDate fechaVisualizacion;

    // Constructor protegido requerido por JPA/Hibernate para @Embeddable
    protected CapituloVisto() {}

    public CapituloVisto(Capitulo capitulo, LocalDate fechaVisualizacion) {
        this.capitulo           = Objects.requireNonNull(capitulo,           "El capítulo no puede ser nulo");
        this.fechaVisualizacion = Objects.requireNonNull(fechaVisualizacion, "La fecha no puede ser nula");
    }

    // ── Object overrides ──────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CapituloVisto cv)) return false;
        return Objects.equals(capitulo, cv.capitulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capitulo);
    }

    @Override
    public int compareTo(CapituloVisto other) {
        int cmp = this.fechaVisualizacion.compareTo(other.fechaVisualizacion);
        return cmp != 0 ? cmp : this.capitulo.compareTo(other.capitulo);
    }

    @Override
    public String toString() {
        return String.format("CapituloVisto{capitulo=%s, fecha=%s}", capitulo, fechaVisualizacion);
    }
}
