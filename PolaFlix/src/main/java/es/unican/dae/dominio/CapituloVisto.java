package es.unican.dae.dominio;

import lombok.Getter;


import java.time.LocalDate;
import java.util.Objects;

@Getter
public class CapituloVisto implements Comparable<CapituloVisto> {

    private final Capitulo  capitulo;
    private final LocalDate fechaVisualizacion;

    public CapituloVisto(Capitulo capitulo, LocalDate fechaVisualizacion) {
        this.capitulo           = Objects.requireNonNull(capitulo,           "El capítulo no puede ser nulo");
        this.fechaVisualizacion = Objects.requireNonNull(fechaVisualizacion, "La fecha no puede ser nula");
    }

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
