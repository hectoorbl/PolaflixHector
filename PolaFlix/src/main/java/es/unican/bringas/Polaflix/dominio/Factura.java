package es.unican.bringas.Polaflix.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "facturas")
@Getter
@NoArgsConstructor
public class Factura implements Comparable<Factura> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private int mes;
    private int anio;

    /*
     * Cada visualización de un capítulo genera SIEMPRE una línea de factura,
     * incluso si ese mismo capítulo ya se había visto antes el mismo día.
     *
     * Por eso esta colección es una List y NO un SortedSet/TreeSet: un Set
     * descartaría como "duplicada" la segunda visualización del mismo capítulo
     * (mismo día, misma serie, misma temporada y mismo número), porque el
     * compareTo/equals de LineaFactura las consideraba iguales. Con una List
     * cada visualización es una fila independiente.
     *
     * El orden de presentación lo da @OrderBy a nivel de base de datos, de modo
     * que las líneas siguen llegando ordenadas por fecha y serie sin depender
     * del compareTo en memoria.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "factura_id")
    @OrderBy("fechaVisualizacion ASC, tituloSerie ASC, numeroTemporada ASC, numeroCapitulo ASC")
    private List<LineaFactura> lineas = new ArrayList<>();

    public Factura(@NonNull Usuario usuario, int mes, int anio) {
        if (mes < 1 || mes > 12) throw new IllegalArgumentException("mes en [1,12]");
        if (anio < 1)            throw new IllegalArgumentException("anio >= 1");
        this.usuario = usuario;
        this.mes     = mes;
        this.anio    = anio;
    }

    public LineaFactura añadirLineaFactura(@NonNull LocalDate fecha, @NonNull Serie serie,
                                            int numTemporada, @NonNull Capitulo capitulo) {
        LineaFactura linea = new LineaFactura(fecha, serie, numTemporada, capitulo);
        lineas.add(linea);
        return linea;
    }

    public double calcularImporte(@NonNull TipoTarifa tarifa) {
        return switch (tarifa) {
            case PLANA        -> lineas.isEmpty() ? 0.0 : TipoTarifa.CUOTA_PLANA;
            case POR_CAPITULO -> lineas.stream().mapToDouble(LineaFactura::getCargo).sum();
        };
    }

    public List<LineaFactura> getLineas() { return Collections.unmodifiableList(lineas); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Factura f)) return false;
        return mes == f.mes && anio == f.anio && usuario.equals(f.usuario);
    }

    @Override
    public int hashCode() { return Objects.hash(usuario, mes, anio); }

    // Orden DESC por (anio, mes): la más reciente queda primero (= first()).
    @Override
    public int compareTo(Factura o) {
        int cmp = Integer.compare(o.anio, this.anio);
        if (cmp != 0) return cmp;
        return Integer.compare(o.mes, this.mes);
    }
}
