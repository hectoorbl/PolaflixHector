package es.unican.bringas.Polaflix.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

@Entity
@Table(name = "temporadas")
@Getter
@NoArgsConstructor
public class Temporada {

    // IDENTITY: la clave la genera la columna autoincremental de la BD (H2/MySQL).
    // Es la opcion mas simple y suficiente para el volumen de la app; no requiere
    // secuencia ni tabla auxiliar como SEQUENCE o TABLE.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "temporada_id")
    @MapKey(name = "numero")
    private SortedMap<Integer, Capitulo> capitulos = new TreeMap<>();

    public Temporada(int numero) {
        if (numero < 1) throw new IllegalArgumentException("numero >= 1");
        this.numero = numero;
    }

    public void addCapitulo(@NonNull Capitulo capitulo) {
        if (capitulos.containsKey(capitulo.getNumero()))
            throw new IllegalArgumentException("Capitulo " + capitulo.getNumero() + " ya existe");
        capitulos.put(capitulo.getNumero(), capitulo);
    }

    public Optional<Capitulo> getCapitulo(int num) {
        return Optional.ofNullable(capitulos.get(num));
    }

    public int numCapitulos() { return capitulos.size(); }

    // Entidad local del agregado Catalogo: el numero la identifica de forma unica
    // dentro de su Serie padre (donde se almacena en un SortedMap por numero).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Temporada t)) return false;
        return numero == t.numero;
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
