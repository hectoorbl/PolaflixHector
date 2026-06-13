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
public class LineaFactura {

    // IDENTITY: la clave la genera la columna autoincremental de la BD (H2/MySQL).
    // Aqui ademas es necesaria una clave subrogada propia: el contenido puede
    // repetirse (mismo capitulo visto dos veces el mismo dia = dos lineas), asi
    // que la identidad NO puede basarse en los datos, solo en el id generado.
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
     * Identidad por id subrogado, NO por contenido. Cada visualizacion es una
     * entidad distinta aunque coincida en fecha, serie, temporada y capitulo con
     * otra (re-ver un capitulo el mismo dia genera otra linea de cobro).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineaFactura lf)) return false;
        return id != null && id.equals(lf.id);
    }

    @Override
    public int hashCode() {
        // hashCode estable para entidades con id generado: no depende de campos
        // que cambian al persistir.
        return getClass().hashCode();
    }
}
