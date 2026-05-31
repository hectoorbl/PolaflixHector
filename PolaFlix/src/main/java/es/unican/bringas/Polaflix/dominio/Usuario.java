package es.unican.bringas.Polaflix.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "usuarios")
@Getter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    @Setter private String contrasena;
    @Setter private String cuentaBancariaIBAN;

    @Setter
    @Enumerated(EnumType.STRING)
    private TipoTarifa tarifa;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    @MapKey(name = "serie")
    private Map<Serie, UsuarioSerie> series = new LinkedHashMap<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuario")
    private SortedSet<Factura> facturas = new TreeSet<>();

    public Usuario(@NonNull String nombreUsuario, @NonNull String contrasena,
                   @NonNull String cuentaBancariaIBAN, @NonNull TipoTarifa tarifa) {
        this.nombreUsuario      = nombreUsuario;
        this.contrasena         = contrasena;
        this.cuentaBancariaIBAN = cuentaBancariaIBAN;
        this.tarifa             = tarifa;
    }

    public void agregarSerie(@NonNull Serie serie) {
        series.putIfAbsent(serie, new UsuarioSerie(serie));
    }

    public Optional<UsuarioSerie> getUsuarioSerie(@NonNull Serie serie) {
        return Optional.ofNullable(series.get(serie));
    }

    public LineaFactura visualizarCapitulo(@NonNull Serie serie, int numTemporada, @NonNull Capitulo capitulo) {
        if (numTemporada < 1) throw new IllegalArgumentException("numTemporada >= 1");
        agregarSerie(serie);
        LocalDate hoy = LocalDate.now();
        series.get(serie).marcarCapituloVisto(numTemporada, capitulo.getNumero(), hoy);
        return obtenerOCrearFacturaActual(hoy).añadirLineaFactura(hoy, serie, numTemporada, capitulo);
    }

    public Factura agregarFactura(int anio, int mes) {
        if (mes < 1 || mes > 12) throw new IllegalArgumentException("mes en [1,12]");
        if (anio < 1)            throw new IllegalArgumentException("anio >= 1");
        Factura candidata = new Factura(this, mes, anio);
        return facturas.stream()
                .filter(f -> f.equals(candidata))
                .findFirst()
                .orElseGet(() -> { facturas.add(candidata); return candidata; });
    }

    public Optional<Factura> getFactura(int anio, int mes) {
        Factura clave = new Factura(this, mes, anio);
        return facturas.stream().filter(f -> f.equals(clave)).findFirst();
    }

    public Optional<Factura> getFacturaMasReciente() {
        return facturas.isEmpty() ? Optional.empty() : Optional.of(facturas.first());
    }

    private Factura obtenerOCrearFacturaActual(LocalDate fecha) {
        return agregarFactura(fecha.getYear(), fecha.getMonthValue());
    }

    public boolean tieneSerie(@NonNull Serie serie) {
        return series.containsKey(serie);
    }

    public List<UsuarioSerie> obtenerSeriesConEstado(@NonNull EstadoSerie estado) {
        return series.values().stream()
                .filter(us -> us.getEstado() == estado)
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario u)) return false;
        return nombreUsuario.equals(u.nombreUsuario);
    }

    @Override
    public int hashCode() { return Objects.hash(nombreUsuario); }
}
