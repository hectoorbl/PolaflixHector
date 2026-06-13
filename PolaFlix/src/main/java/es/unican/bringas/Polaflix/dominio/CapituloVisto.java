package es.unican.bringas.Polaflix.dominio;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Objects;

// Value object (@Embeddable): no tiene identidad propia ni clave, su identidad
// es por valor. Por eso NO lleva @Id ni @GeneratedValue.
@Embeddable
@Getter
@NoArgsConstructor
public final class CapituloVisto implements Comparable<CapituloVisto> {

    private int       numTemporada;
    private int       numCapitulo;
    private LocalDate fechaVisualizacion;

    // Unico punto donde se valida la precondicion (numTemporada/numCapitulo >= 1).
    public CapituloVisto(int numTemporada, int numCapitulo, @NonNull LocalDate fechaVisualizacion) {
        if (numTemporada < 1) throw new IllegalArgumentException("numTemporada >= 1");
        if (numCapitulo  < 1) throw new IllegalArgumentException("numCapitulo >= 1");
        this.numTemporada       = numTemporada;
        this.numCapitulo        = numCapitulo;
        this.fechaVisualizacion = fechaVisualizacion;
    }

    // Value object: igualdad por valor (los tres campos que lo componen).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CapituloVisto cv)) return false;
        return numTemporada == cv.numTemporada
                && numCapitulo == cv.numCapitulo
                && fechaVisualizacion.equals(cv.fechaVisualizacion);
    }

    @Override
    public int hashCode() { return Objects.hash(numTemporada, numCapitulo, fechaVisualizacion); }

    @Override
    public int compareTo(CapituloVisto o) {
        int cmp = Integer.compare(this.numTemporada, o.numTemporada);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(this.numCapitulo, o.numCapitulo);
        if (cmp != 0) return cmp;
        return this.fechaVisualizacion.compareTo(o.fechaVisualizacion);
    }
}
