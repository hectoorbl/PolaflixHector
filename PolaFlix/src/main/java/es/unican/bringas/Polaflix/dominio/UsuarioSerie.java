package es.unican.bringas.Polaflix.repositorio.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(
    name = "usuario_series",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_usuario_serie",
        columnNames = {"usuario_id", "serie_id"}
    )
)
@Getter
@Setter
public class UsuarioSerie implements Comparable<UsuarioSerie> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Lado PROPIETARIO de la relación N:1 con Usuario.
     * UsuarioSerie tiene la FK usuario_id en su tabla.
     * Sin cascade: Usuario se gestiona de forma independiente.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Lado PROPIETARIO de la relación N:1 con Serie.
     * UsuarioSerie tiene la FK serie_id en su tabla.
     * Sin cascade: Serie tiene ciclo de vida propio, no debe borrarse al dejar de seguirla.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id", nullable = false)
    private Serie serie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSerie estado;

    /**
     * Colección de capítulos vistos almacenada como @ElementCollection.
     * CapituloVisto es @Embeddable, por lo que JPA gestiona automáticamente
     * su ciclo de vida (inserción y borrado) junto con UsuarioSerie.
     * La tabla "capitulos_vistos" contiene una FK a usuario_series(id).
     */
    @ElementCollection
    @CollectionTable(
        name = "capitulos_vistos",
        joinColumns = @JoinColumn(name = "usuario_serie_id")
    )
    @OrderBy("fechaVisualizacion ASC")
    private Set<CapituloVisto> capitulosVistos;

    // Constructor protegido requerido por JPA
    protected UsuarioSerie() {}

    public UsuarioSerie(Serie serie) {
        this.serie           = Objects.requireNonNull(serie, "La serie no puede ser nula");
        this.estado          = EstadoSerie.PENDIENTE;
        this.capitulosVistos = new LinkedHashSet<>();
    }

    // ── Métodos de negocio ────────────────────────────────────────────────────

    public void marcarCapituloVisto(Capitulo capitulo, LocalDate fecha) {
        Objects.requireNonNull(capitulo, "El capítulo no puede ser nulo");
        Objects.requireNonNull(fecha,    "La fecha no puede ser nula");

        if (!capitulo.getSerie().equals(serie)) {
            throw new IllegalArgumentException(
                "El capítulo '" + capitulo + "' no pertenece a la serie '" + serie.getTitulo() + "'");
        }

        CapituloVisto cv = new CapituloVisto(capitulo, fecha);
        if (!capitulosVistos.add(cv)) {
            throw new IllegalStateException("El capítulo '" + capitulo + "' ya está marcado como visto");
        }

        if (estado == EstadoSerie.PENDIENTE) {
            estado = EstadoSerie.EMPEZADA;
        }
        if (capitulo.equals(serie.getUltimoCapitulo())) {
            estado = EstadoSerie.TERMINADA;
        }
    }

    public Capitulo ultimoCapituloVisto() {
        if (capitulosVistos.isEmpty()) {
            throw new NoSuchElementException("No se ha visto ningún capítulo de '" + serie.getTitulo() + "'");
        }
        CapituloVisto ultimo = null;
        for (CapituloVisto cv : capitulosVistos) ultimo = cv;
        return ultimo.getCapitulo();
    }

    public boolean estaVisto(Capitulo capitulo) {
        return capitulosVistos.contains(new CapituloVisto(capitulo, LocalDate.MIN));
    }

    // ── Object overrides ──────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioSerie us)) return false;
        return Objects.equals(serie, us.serie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serie);
    }

    @Override
    public int compareTo(UsuarioSerie other) {
        return this.serie.compareTo(other.serie);
    }

    @Override
    public String toString() {
        return String.format("UsuarioSerie{serie='%s', estado=%s, vistos=%d}",
                serie.getTitulo(), estado, capitulosVistos.size());
    }
}
