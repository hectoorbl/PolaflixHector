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
public class Usuario implements Comparable<Usuario> {

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

    // Mapa por título de serie; UsuarioSerie tiene su propio @Entity con cascade
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    @MapKey(name = "serie")
    private final LinkedHashMap<String, UsuarioSerie> series = new LinkedHashMap<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuario")
    private final TreeSet<Factura> facturas = new TreeSet<>();

    public Usuario(@NonNull String nombreUsuario, @NonNull String contrasena,
                   @NonNull String cuentaBancariaIBAN, @NonNull TipoTarifa tarifa) {
        this.nombreUsuario      = nombreUsuario;
        this.contrasena         = contrasena;
        this.cuentaBancariaIBAN = cuentaBancariaIBAN;
        this.tarifa             = tarifa;
    }

    public void agregarSerie(@NonNull Serie serie) {
        series.putIfAbsent(serie.getTitulo(), new UsuarioSerie(serie));
    }

    public Optional<UsuarioSerie> getUsuarioSerie(@NonNull Serie serie) {
        return Optional.ofNullable(series.get(serie.getTitulo()));
    }

    public void visualizarCapitulo(@NonNull Serie serie, int numTemporada, @NonNull Capitulo capitulo) {
        assert numTemporada >= 1 : "numTemporada >= 1";
        agregarSerie(serie);
        LocalDate hoy = LocalDate.now();
        series.get(serie.getTitulo()).marcarCapituloVisto(numTemporada, capitulo.getNumero(), hoy, serie.totalCapitulos());
        obtenerOCrearFacturaActual(hoy).añadirLineaFactura(hoy, serie, numTemporada, capitulo);
    }

    public Factura agregarFactura(int anio, int mes) {
        assert mes >= 1 && mes <= 12 : "mes en [1,12]";
        assert anio >= 1             : "anio >= 1";
        Factura candidata = new Factura(this, mes, anio);
        Factura existente = facturas.floor(candidata);
        if (existente != null && existente.equals(candidata)) return existente;
        facturas.add(candidata);
        return candidata;
    }

    public Optional<Factura> getFactura(int anio, int mes) {
        Factura clave = new Factura(this, mes, anio);
        Factura encontrada = facturas.floor(clave);
        if (encontrada != null && encontrada.equals(clave)) return Optional.of(encontrada);
        return Optional.empty();
    }

    public Optional<Factura> getFacturaMasReciente() {
        return facturas.isEmpty() ? Optional.empty() : Optional.of(facturas.first());
    }

    private Factura obtenerOCrearFacturaActual(LocalDate fecha) {
        return agregarFactura(fecha.getYear(), fecha.getMonthValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario u)) return false;
        return nombreUsuario.equals(u.nombreUsuario);
    }

    @Override
    public int hashCode() { return Objects.hash(nombreUsuario); }

    @Override
    public int compareTo(Usuario o) { return this.nombreUsuario.compareToIgnoreCase(o.nombreUsuario); }
}
