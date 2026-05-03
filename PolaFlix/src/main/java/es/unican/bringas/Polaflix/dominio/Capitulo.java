package es.unican.bringas.Polaflix.dominio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class Capitulo implements Comparable<Capitulo> {

    private int numero;
    private String titulo;
    private String descripcion;

    public Capitulo(int numero, @NonNull String titulo, @NonNull String descripcion) {
        assert numero >= 1 : "numero >= 1";
        this.numero      = numero;
        this.titulo      = titulo;
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Capitulo c)) return false;
        return numero == c.numero;
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }

    @Override
    public int compareTo(Capitulo o) { return Integer.compare(this.numero, o.numero); }
}
