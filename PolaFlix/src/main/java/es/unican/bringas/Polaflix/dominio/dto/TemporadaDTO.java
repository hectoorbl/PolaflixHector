package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.unican.bringas.Polaflix.dominio.Temporada;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemporadaDTO {

    private int                 numero;
    private List<CapituloDTO>   capitulos;

    /** Capítulos del catálogo (sin estado de visualización). */
    public static TemporadaDTO de(Temporada t) {
        TemporadaDTO dto = new TemporadaDTO();
        dto.numero    = t.getNumero();
        dto.capitulos = t.getCapitulos().values().stream().map(CapituloDTO::de).toList();
        return dto;
    }

    /** Capítulos con estado de visualización (espacio personal del usuario). */
    public static TemporadaDTO conCapitulos(int numero, List<CapituloDTO> capitulos) {
        TemporadaDTO dto = new TemporadaDTO();
        dto.numero    = numero;
        dto.capitulos = capitulos;
        return dto;
    }
}
