package es.unican.dae.dominio;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
public class UsuarioSerie implements Comparable<UsuarioSerie> {

    private final Serie serie;
    private EstadoSerie estado;
    private final LinkedHashSet<CapituloVisto> capitulosVistos;

    public UsuarioSerie(Serie serie) {
        this.serie = Objects.requireNonNull(serie, "La serie no puede ser nula");
        this.estado = EstadoSerie.PENDIENTE;
        this.capitulosVistos = new LinkedHashSet<>();
    }

    public void marcarCapituloVisto(Capitulo capitulo, LocalDate fecha) {
        Objects.requireNonNull(capitulo, "El capítulo no puede ser nulo");
        Objects.requireNonNull(fecha,    "La fecha no puede ser nula");

        if (!capitulo.getSerie().equals(serie)) {
            throw new IllegalArgumentException(
                "El capítulo '" + capitulo + "' no pertenece a la serie '" + serie.getTitulo() + "'");
        }

        CapituloVisto cv = new CapituloVisto(capitulo, fecha);
        if (!capitulosVistos.add(cv)) {
            throw new IllegalStateException("El capítulo '" + capitulo + "' ya está marcado como visto");
        }

        if (estado == EstadoSerie.PENDIENTE) {
            estado = EstadoSerie.EMPEZADA;
        }
        if (capitulo.equals(serie.getUltimoCapitulo())) {
            estado = EstadoSerie.TERMINADA;
        }
    }

    public Capitulo ultimoCapituloVisto() {
        if (capitulosVistos.isEmpty()) {
            throw new NoSuchElementException("No se ha visto ningún capítulo de '" + serie.getTitulo() + "'");
        }
        CapituloVisto ultimo = null;
        for (CapituloVisto cv : capitulosVistos) ultimo = cv;
        return ultimo.getCapitulo();
    }

    public boolean estaVisto(Capitulo capitulo) {
        return capitulosVistos.contains(new CapituloVisto(capitulo, LocalDate.MIN));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioSerie us)) return false;
        return Objects.equals(serie, us.serie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serie);
    }

    @Override
    public int compareTo(UsuarioSerie other) {
        return this.serie.compareTo(other.serie);
    }

    @Override
    public String toString() {
        return String.format("UsuarioSerie{serie='%s', estado=%s, vistos=%d}",
                serie.getTitulo(), estado, capitulosVistos.size());
    }
}
