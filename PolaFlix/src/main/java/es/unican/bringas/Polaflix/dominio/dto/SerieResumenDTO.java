package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.CategoriaSerie;
import es.unican.bringas.Polaflix.dominio.Serie;
import lombok.Getter;

@Getter
public class SerieResumenDTO {

    @JsonProperty("titulo")
    private final String titulo;

    @JsonProperty("categoria")
    private final CategoriaSerie categoria;

    public SerieResumenDTO(Serie serie) {
        this.titulo    = serie.getTitulo();
        this.categoria = serie.getCategoria();
    }
}
