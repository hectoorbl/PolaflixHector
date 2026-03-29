package es.unican.dae.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario implements Comparable<Usuario> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombreUsuario;

    @Column(nullable = false)
    private String contraseña;

    @Column(nullable = false)
    private String cuentaBancariaIBAN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTarifa tarifa;

    /**
     * Relación bidireccional 1:N con UsuarioSerie.
     * Usuario es el lado INVERSO (mappedBy = "usuario").
     * UsuarioSerie es el lado propietario y tiene las FKs.
     * cascade = ALL + orphanRemoval: si se borra el usuario, se borran sus seguimientos.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioSerie> series;

    /**
     * Relación bidireccional 1:N con Factura.
     * Usuario es el lado INVERSO (mappedBy = "usuario").
     * Factura es el lado propietario y tiene la FK usuario_id.
     * cascade = ALL + orphanRemoval: las facturas se destruyen con el usuario.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("anyo ASC, mes ASC")
    private List<Factura> facturas;

    // Constructor protegido requerido por JPA
    protected Usuario() {}

    public Usuario(String nombreUsuario, String contraseña, String cuentaBancariaIBAN, TipoTarifa tarifa) {
        if (nombreUsuario == null || nombreUsuario.isBlank())
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        this.nombreUsuario      = nombreUsuario;
        this.contraseña         = Objects.requireNonNull(contraseña,         "La contraseña no puede ser nula");
        this.cuentaBancariaIBAN = Objects.requireNonNull(cuentaBancariaIBAN, "El IBAN no puede ser nulo");
        this.tarifa             = Objects.requireNonNull(tarifa,             "La tarifa no puede ser nula");
        this.series             = new ArrayList<>();
        this.facturas           = new ArrayList<>();
    }

    // ── Métodos de negocio ────────────────────────────────────────────────────

    public void visualizarCapitulo(Serie serie, Capitulo capitulo) {
        Objects.requireNonNull(serie,    "La serie no puede ser nula");
        Objects.requireNonNull(capitulo, "El capítulo no puede ser nulo");

        agregarSerie(serie);

        LocalDate hoy = LocalDate.now();
        getUsuarioSerie(serie).marcarCapituloVisto(capitulo, hoy);

        obtenerOCrearFactura(hoy.getMonthValue(), hoy.getYear())
                .addLinea(serie, capitulo, hoy);
    }

    public void agregarSerie(Serie serie) {
        Objects.requireNonNull(serie, "La serie no puede ser nula");
        boolean existe = series.stream().anyMatch(us -> us.getSerie().equals(serie));
        if (!existe) {
            UsuarioSerie us = new UsuarioSerie(serie);
            us.setUsuario(this);
            series.add(us);
        }
    }

    public List<UsuarioSerie> getSeries(EstadoSerie estado) {
        return series.stream()
                .filter(us -> estado == null || us.getEstado() == estado)
                .sorted()
                .collect(Collectors.toList());
    }

    public Factura getFacturaDelMes(int mes, int año) {
        return facturas.stream()
                .filter(f -> f.getMes() == mes && f.getAño() == año)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                    String.format("No existe factura para %02d/%d del usuario '%s'", mes, año, nombreUsuario)));
    }

    // ── Métodos privados ──────────────────────────────────────────────────────

    private UsuarioSerie getUsuarioSerie(Serie serie) {
        return series.stream()
                .filter(us -> us.getSerie().equals(serie))
                .findFirst()
                .orElseThrow();
    }

    private Factura obtenerOCrearFactura(int mes, int año) {
        return facturas.stream()
                .filter(f -> f.getMes() == mes && f.getAño() == año)
                .findFirst()
                .orElseGet(() -> {
                    Factura f = new Factura(mes, año);
                    f.setUsuario(this);
                    facturas.add(f);
                    return f;
                });
    }

    // ── Object overrides ──────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario u)) return false;
        return nombreUsuario.equalsIgnoreCase(u.nombreUsuario);
    }

    @Override
    public int hashCode() {
        return nombreUsuario.toLowerCase().hashCode();
    }

    @Override
    public int compareTo(Usuario other) {
        return this.nombreUsuario.compareToIgnoreCase(other.nombreUsuario);
    }

    @Override
    public String toString() {
        return String.format("Usuario{nombre='%s', tarifa=%s}", nombreUsuario, tarifa);
    }
}
