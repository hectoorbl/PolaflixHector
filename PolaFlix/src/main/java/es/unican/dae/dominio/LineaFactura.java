package es.unican.dae.dominio;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class LineaFactura implements Comparable<LineaFactura> {

    private final Serie     serie;
    private final Capitulo  capitulo;
    private final LocalDate fechaVisualizacion;
    private final double    cargo;

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
