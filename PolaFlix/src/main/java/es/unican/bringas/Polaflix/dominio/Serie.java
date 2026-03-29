package es.unican.dae.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "series")
@Getter
@Setter
public class Serie implements Comparable<Serie> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titulo;

    @Column(length = 2000)
    private String sinopsis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaSerie categoria;

    @ElementCollection
    @CollectionTable(name = "serie_creadores", joinColumns = @JoinColumn(name = "serie_id"))
    @Column(name = "creador", nullable = false)
    private List<String> creadores;

    @ElementCollection
    @CollectionTable(name = "serie_actores", joinColumns = @JoinColumn(name = "serie_id"))
    @Column(name = "actor", nullable = false)
    private List<String> actoresPrincipales;

    /**
     * Relación bidireccional 1:N con Capitulo.
     * Serie es el lado inverso (mappedBy); Capitulo tiene la FK serie_id.
     * cascade = ALL + orphanRemoval: los capítulos no tienen sentido sin su serie.
     */
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Capitulo> capitulos;

    // Constructor protegido requerido por JPA
    protected Serie() {}

    public Serie(String titulo, String sinopsis, CategoriaSerie categoria) {
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("El título no puede estar vacío");
        Objects.requireNonNull(categoria, "La categoría no puede ser nula");
        this.titulo             = titulo;
        this.sinopsis           = sinopsis == null ? "" : sinopsis;
        this.categoria          = categoria;
        this.creadores          = new ArrayList<>();
        this.actoresPrincipales = new ArrayList<>();
        this.capitulos          = new HashSet<>();
    }

    // ── Métodos de negocio ────────────────────────────────────────────────────

    public void addCapitulo(Capitulo capitulo) {
        Objects.requireNonNull(capitulo, "El capítulo no puede ser nulo");
        boolean added = capitulos.add(capitulo);
        if (!added) {
            throw new IllegalArgumentException(
                "Ya existe el capítulo " + capitulo.getNumero() + " en la temporada " + capitulo.getTemporada());
        }
        capitulo.setSerie(this);
    }

    public List<Capitulo> getCapitulosPorTemporada(int temporada) {
        return capitulos.stream()
                .filter(c -> c.getTemporada() == temporada)
                .sorted()
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Integer> getTemporadas() {
        return capitulos.stream()
                .map(Capitulo::getTemporada)
                .distinct()
                .sorted()
                .collect(java.util.stream.Collectors.toList());
    }

    public Capitulo getUltimoCapitulo() {
        return capitulos.stream()
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new NoSuchElementException("La serie '" + titulo + "' no tiene capítulos"));
    }

    public Optional<Capitulo> getCapitulo(int temporada, int numero) {
        return capitulos.stream()
                .filter(c -> c.getTemporada() == temporada && c.getNumero() == numero)
                .findFirst();
    }

    public void addCreador(String creador) { if (creador != null && !creador.isBlank()) creadores.add(creador); }
    public void addActor(String actor)     { if (actor   != null && !actor.isBlank())   actoresPrincipales.add(actor); }

    // ── Object overrides ──────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Serie s)) return false;
        return titulo.equalsIgnoreCase(s.titulo);
    }

    @Override
    public int hashCode() {
        return titulo.toLowerCase().hashCode();
    }

    @Override
    public int compareTo(Serie other) {
        return this.titulo.compareToIgnoreCase(other.titulo);
    }

    @Override
    public String toString() {
        return String.format("Serie{titulo='%s', categoria=%s}", titulo, categoria);
    }
}
