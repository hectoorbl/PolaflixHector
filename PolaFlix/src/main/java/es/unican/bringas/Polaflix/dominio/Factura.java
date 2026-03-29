package es.unican.dae.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(
    name = "facturas",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_factura_usuario_mes_anyo",
        columnNames = {"usuario_id", "mes", "anyo"}
    )
)
@Getter
@Setter
public class Factura implements Comparable<Factura> {

    public static final double CUOTA_PLANA = 20.00;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int mes;

    /**
     * "año" es palabra reservada en algunos dialectos SQL; se mapea como "anyo"
     * para evitar conflictos con el DDL generado.
     */
    @Column(name = "anyo", nullable = false)
    private int año;

    /**
     * Lado PROPIETARIO de la relación N:1 con Usuario.
     * Factura tiene la FK usuario_id en su tabla.
     * Sin cascade desde Factura hacia Usuario.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Colección de líneas de factura almacenada como @ElementCollection.
     * LineaFactura es @Embeddable, por lo que JPA gestiona automáticamente
     * su ciclo de vida junto con Factura.
     * La tabla "lineas_factura" contiene una FK a facturas(id).
     */
    @ElementCollection
    @CollectionTable(
        name = "lineas_factura",
        joinColumns = @JoinColumn(name = "factura_id")
    )
    @OrderBy("fechaVisualizacion ASC")
    private Set<LineaFactura> lineas;

    // Constructor protegido requerido por JPA
    protected Factura() {}

    public Factura(int mes, int año) {
        if (mes < 1 || mes > 12) throw new IllegalArgumentException("Mes inválido: " + mes);
        if (año < 2000)          throw new IllegalArgumentException("Año inválido: " + año);
        this.mes    = mes;
        this.año    = año;
        this.lineas = new TreeSet<>();
    }

    // ── Métodos de negocio ────────────────────────────────────────────────────

    public void addLinea(Serie serie, Capitulo capitulo, LocalDate fecha) {
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

    // ── Object overrides ──────────────────────────────────────────────────────

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
