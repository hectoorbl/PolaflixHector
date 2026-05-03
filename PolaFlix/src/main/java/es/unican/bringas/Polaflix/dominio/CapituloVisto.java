package es.unican.bringas.Polaflix.dominio;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Objects;


@Getter
public final class CapituloVisto implements Comparable<CapituloVisto> {

    private final int numTemporada;
    private final int numCapitulo;
    private final LocalDate fechaVisualizacion;

    public CapituloVisto(int numTemporada, int numCapitulo, @NonNull LocalDate fechaVisualizacion) {
        assert numTemporada >= 1 : "numTemporada >= 1";
        assert numCapitulo  >= 1 : "numCapitulo >= 1";
        this.numTemporada       = numTemporada;
        this.numCapitulo        = numCapitulo;
        this.fechaVisualizacion = fechaVisualizacion;
    }

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