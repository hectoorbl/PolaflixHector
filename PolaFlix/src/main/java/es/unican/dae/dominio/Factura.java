package es.unican.dae.dominio;

import lombok.Getter;
import lombok.Setter;


import java.util.*;

@Getter
@Setter
public class Factura implements Comparable<Factura> {

    public static final double CUOTA_PLANA = 20.00;

    private final int mes;
    private final int año;
    private final TreeSet<LineaFactura> lineas;

    public Factura(int mes, int año) {
        if (mes < 1 || mes > 12) throw new IllegalArgumentException("Mes inválido: " + mes);
        if (año < 2000)          throw new IllegalArgumentException("Año inválido: " + año);
        this.mes    = mes;
        this.año    = año;
        this.lineas = new TreeSet<>();
    }

    public void addLinea(Serie serie, Capitulo capitulo, java.time.LocalDate fecha) {
        lineas.add(new LineaFactura(serie, capitulo, fecha));
    }

    public double getTotalConTarifa(TipoTarifa tarifa) {
        return switch (tarifa) {
            case POR_CAPITULO -> calcularImporte();
            case PLANA        -> CUOTA_PLANA;
        };
    }

    public double calcularImporte() {
        return lineas.stream().mapToDouble(LineaFactura::getCargo).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Factura f)) return false;
        return mes == f.mes && año == f.año;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mes, año);
    }

    @Override
    public int compareTo(Factura other) {
        int cmp = Integer.compare(this.año, other.año);
        return cmp != 0 ? cmp : Integer.compare(this.mes, other.mes);
    }

    @Override
    public String toString() {
        return String.format("Factura{%02d/%d, lineas=%d, importe=%.2f€}", mes, año, lineas.size(), calcularImporte());
    }
}