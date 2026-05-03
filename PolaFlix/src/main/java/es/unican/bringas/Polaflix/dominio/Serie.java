package es.unican.bringas.Polaflix.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;

@Entity
@Table(name = "series")
@Getter
@NoArgsConstructor
public class Serie implements Comparable<Serie> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String titulo;

    @Setter private String sinopsis;

    @Setter
    @Enumerated(EnumType.STRING)
    private CategoriaSerie categoria;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "serie_id")
    @MapKey(name = "numero")
    private final TreeMap<Integer, Temporada> temporadas = new TreeMap<>();

    // Persona es @Embeddable; usamos @ElementCollection con tabla de unión
    @ElementCollection
    @CollectionTable(name = "serie_actores", joinColumns = @JoinColumn(name = "serie_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "nombre",   column = @Column(name = "nombre")),
        @AttributeOverride(name = "apellido", column = @Column(name = "apellido"))
    })
    private final TreeSet<Persona> actores = new TreeSet<>();

    @ElementCollection
    @CollectionTable(name = "serie_creadores", joinColumns = @JoinColumn(name = "serie_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "nombre",   column = @Column(name = "nombre")),
        @AttributeOverride(name = "apellido", column = @Column(name = "apellido"))
    })
    private final TreeSet<Persona> creadores = new TreeSet<>();

    public Serie(@NonNull String titulo, @NonNull String sinopsis, @NonNull CategoriaSerie categoria) {
        this.titulo    = titulo;
        this.sinopsis  = sinopsis;
        this.categoria = categoria;
    }

    public void addTemporada(@NonNull Temporada temporada) {
        if (temporadas.containsKey(temporada.getNumero()))
            throw new IllegalArgumentException("Temporada " + temporada.getNumero() + " ya existe");
        temporadas.put(temporada.getNumero(), temporada);
    }

    public Optional<Temporada> getTemporada(int num) {
        return Optional.ofNullable(temporadas.get(num));
    }

    public Optional<Capitulo> getCapitulo(int nTemp, int nCap) {
        return getTemporada(nTemp).flatMap(t -> t.getCapitulo(nCap));
    }

    public void addActor(@NonNull Persona a)   { actores.add(a); }
    public void addCreador(@NonNull Persona c) { creadores.add(c); }

    public int totalCapitulos() {
        return temporadas.values().stream().mapToInt(Temporada::numCapitulos).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Serie s)) return false;
        return titulo.equals(s.titulo);
    }

    @Override
    public int hashCode() { return Objects.hash(titulo); }

    @Override
    public int compareTo(Serie o) { return this.titulo.compareToIgnoreCase(o.titulo); }
}
