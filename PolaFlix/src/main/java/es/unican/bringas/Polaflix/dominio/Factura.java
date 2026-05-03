package es.unican.bringas.Polaflix.dominio;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
public class Factura implements Comparable<Factura> {

    private final Usuario usuario;
    private final int     mes;
    private final int     anio;

    private final TreeSet<LineaFactura> lineas = new TreeSet<>();

    public Factura(@NonNull Usuario usuario, int mes, int anio) {
        assert mes >= 1 && mes <= 12 : "mes en [1,12]";
        assert anio >= 1             : "anio >= 1";
        this.usuario = usuario;
        this.mes     = mes;
        this.anio    = anio;
    }

    public LineaFactura añadirLineaFactura(@NonNull LocalDate fecha, @NonNull Serie serie,
                                            int numTemporada, @NonNull Capitulo capitulo) {
        LineaFactura linea = LineaFactura.crear(fecha, serie, numTemporada, capitulo);
        lineas.add(linea);
        return linea;
    }

    public double calcularImporte(@NonNull TipoTarifa tarifa) {
        return switch (tarifa) {
            case PLANA        -> lineas.isEmpty() ? 0.0 : TipoTarifa.CUOTA_PLANA;
            case POR_CAPITULO -> lineas.stream().mapToDouble(LineaFactura::getCargo).sum();
        };
    }

    public SortedSet<LineaFactura> getLineas() { return Collections.unmodifiableSortedSet(lineas); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Factura f)) return false;
        return mes == f.mes && anio == f.anio && usuario.equals(f.usuario);
    }

    @Override
    public int hashCode() { return Objects.hash(usuario, mes, anio); }

    @Override
    public int compareTo(Factura o) {
        int cmp = Integer.compare(o.anio, this.anio);
        if (cmp != 0) return cmp;
        return Integer.compare(o.mes, this.mes);
    }
}
