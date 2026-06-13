package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.CategoriaSerie;
import es.unican.bringas.Polaflix.dominio.Serie;
import lombok.Getter;

@Getter
public class SerieBuscadaDTO {

    @JsonProperty("titulo")
    private final String titulo;

    @JsonProperty("categoria")
    private final CategoriaSerie categoria;

    @JsonProperty("encontrada")
    private final boolean encontrada;

    public SerieBuscadaDTO(Serie serie, boolean encontrada) {
        this.titulo     = serie.getTitulo();
        this.categoria  = serie.getCategoria();
        this.encontrada = encontrada;
    }
}
