package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.CategoriaSerie;
import es.unican.bringas.Polaflix.dominio.EstadoSerie;
import es.unican.bringas.Polaflix.dominio.Serie;
import es.unican.bringas.Polaflix.dominio.UsuarioSerie;
import lombok.Getter;

import java.util.List;

@Getter
public class SerieUsuarioDTO {

    @JsonProperty("titulo")
    private final String titulo;

    @JsonProperty("categoria")
    private final CategoriaSerie categoria;

    @JsonProperty("estado")
    private final EstadoSerie estado;

    @JsonProperty("temporadas")
    private final List<TemporadaDTO> temporadas;

    public SerieUsuarioDTO(Serie serie, UsuarioSerie us) {
        this.titulo     = serie.getTitulo();
        this.categoria  = serie.getCategoria();
        this.estado     = us.getEstado();
        this.temporadas = serie.getTemporadas().values().stream()
                .map(t -> new TemporadaDTO(t, us))
                .toList();
    }
}
