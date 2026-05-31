package es.unican.bringas.Polaflix.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "usuario_series")
@Getter
@NoArgsConstructor
public class UsuarioSerie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "serie_id", nullable = false)
    private Serie serie;

    @Setter
    @Enumerated(EnumType.STRING)
    private EstadoSerie estado;

    @ElementCollection
    @CollectionTable(name = "capitulos_vistos", joinColumns = @JoinColumn(name = "usuario_serie_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "numTemporada",       column = @Column(name = "num_temporada")),
        @AttributeOverride(name = "numCapitulo",        column = @Column(name = "num_capitulo")),
        @AttributeOverride(name = "fechaVisualizacion", column = @Column(name = "fecha_visualizacion"))
    })
    private SortedSet<CapituloVisto> capitulosVistos = new TreeSet<>();

    public UsuarioSerie(@NonNull Serie serie) {
        this.serie  = serie;
        this.estado = EstadoSerie.PENDIENTE;
    }

    public void marcarCapituloVisto(int numTemporada, int numCapitulo, @NonNull LocalDate fecha) {
        if (numTemporada < 1) throw new IllegalArgumentException("numTemporada >= 1");
        if (numCapitulo  < 1) throw new IllegalArgumentException("numCapitulo >= 1");
        capitulosVistos.add(new CapituloVisto(numTemporada, numCapitulo, fecha));
        if (serie.esUltimoCapitulo(numTemporada, numCapitulo)) {
            estado = EstadoSerie.TERMINADA;
        } else if (estado == EstadoSerie.PENDIENTE) {
            estado = EstadoSerie.EMPEZADA;
        }
    }

    public Optional<CapituloVisto> ultimoCapituloVisto() {
        return capitulosVistos.isEmpty() ? Optional.empty() : Optional.of(capitulosVistos.last());
    }

    public boolean haVisto(int numTemporada, int numCapitulo) {
        return capitulosVistos.stream()
                .anyMatch(cv -> cv.getNumTemporada() == numTemporada
                             && cv.getNumCapitulo()  == numCapitulo);
    }

    public Optional<CapituloVisto> capituloMasAvanzadoVisto() {
        return capitulosVistos.stream()
                .max(Comparator.comparingInt(CapituloVisto::getNumTemporada)
                        .thenComparingInt(CapituloVisto::getNumCapitulo));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioSerie us)) return false;
        return serie.equals(us.serie);
    }

    @Override
    public int hashCode() { return Objects.hash(serie); }
}
