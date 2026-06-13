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

    // IDENTITY: la clave la genera la columna autoincremental de la BD (H2/MySQL).
    // Es la opcion mas simple y suficiente para el volumen de la app; no requiere
    // secuencia ni tabla auxiliar como SEQUENCE o TABLE.
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

    // Entidad local del agregado Catalogo: el numero lo identifica de forma unica
    // dentro de su Temporada padre (donde se almacena en un SortedMap por numero).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Capitulo c)) return false;
        return numero == c.numero;
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
