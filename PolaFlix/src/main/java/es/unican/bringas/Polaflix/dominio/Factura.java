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

    // IDENTITY: la clave la genera la columna autoincremental de la BD (H2/MySQL).
    // Es la opcion mas simple y suficiente para el volumen de la app; no requiere
    // secuencia ni tabla auxiliar como SEQUENCE o TABLE.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private int mes;
    private int anio;

    /*
     * Cada visualizacion de un capitulo genera SIEMPRE una linea de factura,
     * incluso si ese mismo capitulo ya se habia visto antes el mismo dia.
     * Por eso es una List (bag) y no un Set: un Set descartaria como duplicada
     * la segunda visualizacion del mismo capitulo. El orden de presentacion lo
     * da @OrderBy a nivel de BD.
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

    // Identidad por clave de negocio: una factura por usuario, mes y anio.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Factura f)) return false;
        return mes == f.mes && anio == f.anio && usuario.equals(f.usuario);
    }

    @Override
    public int hashCode() { return Objects.hash(usuario, mes, anio); }

    // Orden DESC por (anio, mes): la mas reciente queda primero (= first()).
    @Override
    public int compareTo(Factura o) {
        int cmp = Integer.compare(o.anio, this.anio);
        if (cmp != 0) return cmp;
        return Integer.compare(o.mes, this.mes);
    }
}
