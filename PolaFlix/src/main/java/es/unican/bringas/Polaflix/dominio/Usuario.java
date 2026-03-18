package es.unican.dae.dominio;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Usuario implements Comparable<Usuario> {

    private final String nombreUsuario;
    private String contraseña;
    private String cuentaBancariaIBAN;
    private TipoTarifa tarifa;

    private final LinkedHashMap<Serie, UsuarioSerie> series;
    private final TreeMap<Integer, Factura>          facturas;

    public Usuario(String nombreUsuario, String contraseña, String cuentaBancariaIBAN, TipoTarifa tarifa) {
        if (nombreUsuario == null || nombreUsuario.isBlank())
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        this.nombreUsuario      = nombreUsuario;
        this.contraseña         = Objects.requireNonNull(contraseña,         "La contraseña no puede ser nula");
        this.cuentaBancariaIBAN = Objects.requireNonNull(cuentaBancariaIBAN, "El IBAN no puede ser nulo");
        this.tarifa             = Objects.requireNonNull(tarifa,             "La tarifa no puede ser nula");
        this.series   = new LinkedHashMap<>();
        this.facturas = new TreeMap<>();
    }

    public void visualizarCapitulo(Serie serie, Capitulo capitulo) {
        Objects.requireNonNull(serie,    "La serie no puede ser nula");
        Objects.requireNonNull(capitulo, "El capítulo no puede ser nulo");

        agregarSerie(serie);

        LocalDate hoy = LocalDate.now();
        series.get(serie).marcarCapituloVisto(capitulo, hoy);

        obtenerOCrearFactura(hoy.getMonthValue(), hoy.getYear())
                .addLinea(serie, capitulo, hoy);
    }

    public void agregarSerie(Serie serie) {
        Objects.requireNonNull(serie, "La serie no puede ser nula");
        series.putIfAbsent(serie, new UsuarioSerie(serie));
    }

    public List<UsuarioSerie> getSeries(EstadoSerie estado) {
        return series.values().stream()
                .filter(us -> estado == null || us.getEstado() == estado)
                .sorted()
                .collect(Collectors.toList());
    }

    public Factura getFacturaDelMes(int mes, int año) {
        Factura resultado = facturas.get(año * 100 + mes);
        if (resultado == null)
            throw new NoSuchElementException(
                    String.format("No existe factura para %02d/%d del usuario '%s'", mes, año, nombreUsuario));
        return resultado;
    }

    private Factura obtenerOCrearFactura(int mes, int año) {
        return facturas.computeIfAbsent(año * 100 + mes, k -> new Factura(mes, año));
    }

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