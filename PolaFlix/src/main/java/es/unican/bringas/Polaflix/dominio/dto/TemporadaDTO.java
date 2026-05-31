package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.Temporada;
import es.unican.bringas.Polaflix.dominio.UsuarioSerie;
import lombok.Getter;

import java.util.List;

@Getter
public class TemporadaDTO {

    @JsonProperty("numero")
    private final int numero;

    @JsonProperty("capitulos")
    private final List<CapituloDTO> capitulos;

    public TemporadaDTO(Temporada t, UsuarioSerie us) {
        this.numero    = t.getNumero();
        this.capitulos = t.getCapitulos().values().stream()
                .map(c -> new CapituloDTO(c, t.getNumero(), us))
                .toList();
    }
}
