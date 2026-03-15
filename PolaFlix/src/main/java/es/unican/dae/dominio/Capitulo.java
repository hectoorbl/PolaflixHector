package es.unican.dae.dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Capitulo implements Comparable<Capitulo> {

    private final int temporada;
    private final int numero;
    private final String titulo;
    private final String descripcion;
    private Serie serie;

    public Capitulo(int temporada, int numero, String titulo, String descripcion) {
        if (temporada < 1) throw new IllegalArgumentException("La temporada debe ser >= 1");
        if (numero    < 1) throw new IllegalArgumentException("El número de capítulo debe ser >= 1");
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("El título no puede estar vacío");
        this.temporada   = temporada;
        this.numero      = numero;
        this.titulo      = titulo;
        this.descripcion = descripcion == null ? "" : descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Capitulo c)) return false;
        return temporada == c.temporada
            && numero    == c.numero
            && Objects.equals(serie, c.serie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serie, temporada, numero);
    }

    @Override
    public int compareTo(Capitulo other) {
        int cmp = Integer.compare(this.temporada, other.temporada);
        return cmp != 0 ? cmp : Integer.compare(this.numero, other.numero);
    }

    @Override
    public String toString() {
        return String.format("T%02dE%02d - %s", temporada, numero, titulo);
    }
}
