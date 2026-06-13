package es.unican.bringas.Polaflix.dominio;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Objects;

// Value object (@Embeddable): no tiene identidad propia ni clave, su identidad
// es por valor (nombre + apellido). Por eso NO lleva @Id ni @GeneratedValue.
@Embeddable
@Getter
@NoArgsConstructor
public class Persona implements Comparable<Persona> {

    private String nombre;
    private String apellido;

    public Persona(@NonNull String nombre, @NonNull String apellido) {
        this.nombre   = nombre;
        this.apellido = apellido;
    }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    // Value object: igualdad por valor (nombre + apellido). Asi una misma persona
    // (p. ej. actor y creador) no se duplica en el sistema.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona p)) return false;
        return nombre.equals(p.nombre) && apellido.equals(p.apellido);
    }

    @Override
    public int hashCode() { return Objects.hash(nombre, apellido); }

    @Override
    public int compareTo(Persona o) {
        int cmp = this.apellido.compareToIgnoreCase(o.apellido);
        if (cmp != 0) return cmp;
        return this.nombre.compareToIgnoreCase(o.nombre);
    }
}
