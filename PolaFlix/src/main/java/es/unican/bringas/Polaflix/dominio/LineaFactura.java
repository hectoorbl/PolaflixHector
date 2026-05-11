package es.unican.bringas.Polaflix.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "lineas_factura")
@Getter
@NoArgsConstructor
public class LineaFactura implements Comparable<LineaFactura> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaVisualizacion;
    private double    cargo;
    private String    tituloSerie;
    private int       numeroTemporada;
    private int       numeroCapitulo;
    private String    tituloCapitulo;

    public LineaFactura(@NonNull LocalDate fechaVisualizacion, @NonNull Serie serie,
                        int numeroTemporada, @NonNull Capitulo capitulo) {
        this.fechaVisualizacion = fechaVisualizacion;
        this.cargo              = serie.getCategoria().getCoste();
        this.tituloSerie        = serie.getTitulo();
        this.numeroTemporada    = numeroTemporada;
        this.numeroCapitulo     = capitulo.getNumero();
        this.tituloCapitulo     = capitulo.getTitulo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineaFactura lf)) return false;
        return numeroTemporada == lf.numeroTemporada
                && numeroCapitulo == lf.numeroCapitulo
                && tituloSerie.equals(lf.tituloSerie)
                && fechaVisualizacion.equals(lf.fechaVisualizacion);
    }

    @Override
    public int hashCode() { return Objects.hash(tituloSerie, numeroTemporada, numeroCapitulo, fechaVisualizacion); }

    @Override
    public int compareTo(LineaFactura o) {
        int cmp = this.fechaVisualizacion.compareTo(o.fechaVisualizacion);
        if (cmp != 0) return cmp;
        cmp = this.tituloSerie.compareToIgnoreCase(o.tituloSerie);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(this.numeroTemporada, o.numeroTemporada);
        if (cmp != 0) return cmp;
        return Integer.compare(this.numeroCapitulo, o.numeroCapitulo);
    }
}
