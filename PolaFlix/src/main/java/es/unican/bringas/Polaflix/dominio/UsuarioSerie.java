package es.unican.bringas.Polaflix.dominio;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

@Getter
public class UsuarioSerie implements Comparable<UsuarioSerie> {


    private final Serie serie;

    @Setter private EstadoSerie estado;

    private final TreeSet<CapituloVisto> capitulosVistos = new TreeSet<>();

    public UsuarioSerie(@NonNull Serie serie) {
        this.serie = serie;
        this.estado  = EstadoSerie.PENDIENTE;
    }

    // El total lo inyecta la capa de aplicación tras consultar el agregado Serie por su repositorio.
    public void marcarCapituloVisto(int numTemporada, int numCapitulo,
                                    @NonNull LocalDate fecha, int totalCapitulosSerie) {
        assert numTemporada >= 1 : "numTemporada >= 1";
        assert numCapitulo  >= 1 : "numCapitulo >= 1";
        capitulosVistos.add(new CapituloVisto(numTemporada, numCapitulo, fecha));
        actualizarEstado(totalCapitulosSerie);
    }

    public Optional<CapituloVisto> ultimoCapituloVisto() {
        return capitulosVistos.isEmpty() ? Optional.empty() : Optional.of(capitulosVistos.last());
    }

    private void actualizarEstado(int totalCapitulos) {
        if (capitulosVistos.isEmpty()) { estado = EstadoSerie.PENDIENTE; return; }
        long unicosVistos = capitulosVistos.stream()
                .map(cv -> cv.getNumTemporada() * 10_000L + cv.getNumCapitulo())
                .distinct().count();
        estado = (totalCapitulos > 0 && unicosVistos >= totalCapitulos)
                ? EstadoSerie.TERMINADA : EstadoSerie.EMPEZADA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioSerie us)) return false;
        return serie.equals(us.serie);
    }

    @Override
    public int hashCode() { return Objects.hash(serie); }

    @Override
    public int compareTo(UsuarioSerie o) { return this.serie.compareTo(o.serie); }
}