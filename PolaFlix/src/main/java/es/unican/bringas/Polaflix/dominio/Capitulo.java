package es.unican.dae.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(
    name = "capitulos",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_capitulo_serie_temporada_numero",
        columnNames = {"serie_id", "temporada", "numero"}
    )
)
@Getter
@Setter
public class Capitulo implements Comparable<Capitulo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int temporada;

    @Column(nullable = false)
    private int numero;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    /**
     * Relación bidireccional N:1 con Serie.
     * Capitulo es el lado PROPIETARIO: tiene la FK serie_id en su tabla.
     * fetch LAZY: evita cargar toda la serie al cargar un capítulo.
     * No se propaga ninguna operación desde Capitulo hacia Serie.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id", nullable = false)
    private Serie serie;

    // Constructor protegido requerido por JPA
    protected Capitulo() {}

    public Capitulo(int temporada, int numero, String titulo, String descripcion) {
        if (temporada < 1) throw new IllegalArgumentException("La temporada debe ser >= 1");
        if (numero    < 1) throw new IllegalArgumentException("El número de capítulo debe ser >= 1");
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("El título no puede estar vacío");
        this.temporada   = temporada;
        this.numero      = numero;
        this.titulo      = titulo;
        this.descripcion = descripcion == null ? "" : descripcion;
    }

    // ── Object overrides ──────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Capitulo c)) return false;
        return temporada == c.temporada
            && numero    == c.numero
            && Objects.equals(serie, c.serie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serie, temporada, numero);
    }

    @Override
    public int compareTo(Capitulo other) {
        int cmp = Integer.compare(this.temporada, other.temporada);
        return cmp != 0 ? cmp : Integer.compare(this.numero, other.numero);
    }

    @Override
    public String toString() {
        return String.format("T%02dE%02d - %s", temporada, numero, titulo);
    }
}
