package es.unican.bringas.Polaflix.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Objects;

@Entity
@Table(name = "capitulos")
@Getter
@NoArgsConstructor
public class Capitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int    numero;
    private String titulo;
    private String descripcion;

    public Capitulo(int numero, @NonNull String titulo, @NonNull String descripcion) {
        if (numero < 1) throw new IllegalArgumentException("numero >= 1");
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
}
