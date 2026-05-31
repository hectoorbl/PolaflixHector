package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.CapituloVisto;
import es.unican.bringas.Polaflix.dominio.CategoriaSerie;
import es.unican.bringas.Polaflix.dominio.UsuarioSerie;
import lombok.Getter;

@Getter
public class SerieEmpezadaDTO {

    @JsonProperty("titulo")
    private final String titulo;

    @JsonProperty("categoria")
    private final CategoriaSerie categoria;

    @JsonProperty("ultimaTemporadaVista")
    private final Integer ultimaTemporadaVista;

    @JsonProperty("ultimoCapituloVisto")
    private final Integer ultimoCapituloVisto;

    public SerieEmpezadaDTO(UsuarioSerie us) {
        this.titulo    = us.getSerie().getTitulo();
        this.categoria = us.getSerie().getCategoria();
        CapituloVisto ultimo = us.capituloMasAvanzadoVisto().orElse(null);
        this.ultimaTemporadaVista = ultimo != null ? ultimo.getNumTemporada() : null;
        this.ultimoCapituloVisto  = ultimo != null ? ultimo.getNumCapitulo()  : null;
    }
}
