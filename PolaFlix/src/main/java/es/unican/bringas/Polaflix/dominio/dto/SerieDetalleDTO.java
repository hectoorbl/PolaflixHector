package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.CategoriaSerie;
import es.unican.bringas.Polaflix.dominio.Serie;
import lombok.Getter;

import java.util.List;

@Getter
public class SerieDetalleDTO {

    @JsonProperty("titulo")
    private final String titulo;

    @JsonProperty("categoria")
    private final CategoriaSerie categoria;

    @JsonProperty("sinopsis")
    private final String sinopsis;

    @JsonProperty("creadores")
    private final List<String> creadores;

    @JsonProperty("actores")
    private final List<String> actores;

    public SerieDetalleDTO(Serie serie) {
        this.titulo    = serie.getTitulo();
        this.categoria = serie.getCategoria();
        this.sinopsis  = serie.getSinopsis();
        this.creadores = serie.nombresCreadores();
        this.actores   = serie.nombresActores();
    }
}
