package es.unican.bringas.Polaflix.dominio;

import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

@Getter
public class Temporada implements Comparable<Temporada> {

    private final int numero;
    private final TreeMap<Integer, Capitulo> capitulos = new TreeMap<>();

    public Temporada(int numero) {
        assert numero >= 1 : "numero >= 1";
        this.numero = numero;
    }

    public void addCapitulo(@NonNull Capitulo capitulo) {
        if (capitulos.containsKey(capitulo.getNumero()))
            throw new IllegalArgumentException("Capítulo " + capitulo.getNumero() + " ya existe");
        capitulos.put(capitulo.getNumero(), capitulo);
    }

    public Optional<Capitulo> getCapitulo(int num) {
        return Optional.ofNullable(capitulos.get(num));
    }

    public int numCapitulos() { return capitulos.size(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Temporada t)) return false;
        return numero == t.numero;
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }

    @Override
    public int compareTo(Temporada o) { return Integer.compare(this.numero, o.numero); }
}
