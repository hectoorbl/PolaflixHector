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

    /*
     * Identidad por id, NO por contenido.
     *
     * Cada visualización es una entidad distinta aunque coincida en fecha,
     * serie, temporada y capítulo con otra (un usuario puede ver el mismo
     * capítulo dos veces el mismo día y se cobra dos veces). Si equals/hashCode
     * se basaran en el contenido, dos visualizaciones idénticas se tratarían
     * como la misma línea y una se perdería al meterlas en colecciones.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineaFactura lf)) return false;
        return id != null && id.equals(lf.id);
    }

    @Override
    public int hashCode() {
        // hashCode estable para entidades con id generado (patrón recomendado
        // por Hibernate): no depende de campos que cambian al persistir.
        return getClass().hashCode();
    }

    /*
     * Orden natural por fecha, serie, temporada y capítulo, con el id como
     * desempate final. El desempate por id es lo que evita que dos líneas
     * idénticas en contenido se consideren "iguales" (compareTo == 0) y se
     * colapsen si alguien las metiera en un TreeSet/SortedSet.
     */
    @Override
    public int compareTo(LineaFactura o) {
        int cmp = this.fechaVisualizacion.compareTo(o.fechaVisualizacion);
        if (cmp != 0) return cmp;
        cmp = this.tituloSerie.compareToIgnoreCase(o.tituloSerie);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(this.numeroTemporada, o.numeroTemporada);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(this.numeroCapitulo, o.numeroCapitulo);
        if (cmp != 0) return cmp;

        // Desempate por id. Las líneas aún no persistidas (id == null) se
        // ordenan al final, pero sin colapsarse entre sí.
        if (this.id == null && o.id == null) return 0;
        if (this.id == null) return 1;
        if (o.id == null)    return -1;
        return this.id.compareTo(o.id);
    }
}
