package es.unican.bringas.Polaflix.dominio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Objects;

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
