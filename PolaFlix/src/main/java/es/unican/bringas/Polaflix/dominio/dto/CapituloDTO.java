package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.Capitulo;
import es.unican.bringas.Polaflix.dominio.UsuarioSerie;
import lombok.Getter;

@Getter
public class CapituloDTO {

    @JsonProperty("numero")
    private final int numero;

    @JsonProperty("titulo")
    private final String titulo;

    @JsonProperty("descripcion")
    private final String descripcion;

    @JsonProperty("visto")
    private final boolean visto;

    public CapituloDTO(Capitulo c, int numTemporada, UsuarioSerie us) {
        this.numero      = c.getNumero();
        this.titulo      = c.getTitulo();
        this.descripcion = c.getDescripcion();
        this.visto       = us.haVisto(numTemporada, c.getNumero());
    }
}
