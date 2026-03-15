package es.unican.dae.dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Serie implements Comparable<Serie> {

    private final String titulo;
    private String sinopsis;
    private final List<String> creadores;
    private final List<String> actoresPrincipales;
    private CategoriaSerie categoria;

    private final TreeMap<Integer, TreeSet<Capitulo>> capitulosPorTemporada;

    public Serie(String titulo, String sinopsis, CategoriaSerie categoria) {
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("El título no puede estar vacío");
        Objects.requireNonNull(categoria, "La categoría no puede ser nula");
        this.titulo                = titulo;
        this.sinopsis              = sinopsis == null ? "" : sinopsis;
        this.categoria             = categoria;
        this.creadores             = new ArrayList<>();
        this.actoresPrincipales    = new ArrayList<>();
        this.capitulosPorTemporada = new TreeMap<>();
    }

    public void addCapitulo(Capitulo capitulo) {
        Objects.requireNonNull(capitulo, "El capítulo no puede ser nulo");
        TreeSet<Capitulo> temporada = capitulosPorTemporada
                .computeIfAbsent(capitulo.getTemporada(), k -> new TreeSet<>());
        if (!temporada.add(capitulo)) {
            throw new IllegalArgumentException(
                "Ya existe el capítulo " + capitulo.getNumero() + " en la temporada " + capitulo.getTemporada());
        }
        capitulo.setSerie(this);
    }

    public List<Capitulo> getCapitulosPorTemporada(int temporada) {
        TreeSet<Capitulo> caps = capitulosPorTemporada.get(temporada);
        if (caps == null) return Collections.emptyList();
        return Collections.unmodifiableList(new ArrayList<>(caps));
    }

    public List<Integer> getTemporadas() {
        return Collections.unmodifiableList(new ArrayList<>(capitulosPorTemporada.keySet()));
    }

    public Capitulo getUltimoCapitulo() {
        if (capitulosPorTemporada.isEmpty()) {
            throw new NoSuchElementException("La serie '" + titulo + "' no tiene capítulos");
        }
        return capitulosPorTemporada.lastEntry().getValue().last();
    }

    public Optional<Capitulo> getCapitulo(int temporada, int numero) {
        TreeSet<Capitulo> caps = capitulosPorTemporada.get(temporada);
        if (caps == null) return Optional.empty();
        return caps.stream().filter(c -> c.getNumero() == numero).findFirst();
    }

    public void addCreador(String creador) { if (creador != null && !creador.isBlank()) creadores.add(creador); }
    public void addActor(String actor)     { if (actor   != null && !actor.isBlank())   actoresPrincipales.add(actor); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Serie s)) return false;
        return titulo.equalsIgnoreCase(s.titulo);
    }

    @Override
    public int hashCode() {
        return titulo.toLowerCase().hashCode();
    }

    @Override
    public int compareTo(Serie other) {
        return this.titulo.compareToIgnoreCase(other.titulo);
    }

    @Override
    public String toString() {
        return String.format("Serie{titulo='%s', categoria=%s}", titulo, categoria);
    }
}
