package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.unican.bringas.Polaflix.dominio.CategoriaSerie;
import es.unican.bringas.Polaflix.dominio.EstadoSerie;
import es.unican.bringas.Polaflix.dominio.Serie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO de Serie polivalente. Cada caso de uso rellena sólo los campos que la
 * API exige; los demás quedan a null y se omiten gracias a {@code @JsonInclude(NON_NULL)}.
 *
 *  - {@link #resumen}    → {titulo, categoria}                        (catálogo, espacio personal)
 *  - {@link #detalle}    → + sinopsis, creadores, actores, temporadas (catálogo detalle)
 *  - {@link #conEstado}  → {titulo, estado, temporadas}               (estado de serie del usuario)
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SerieDTO {

    private String              titulo;
    private CategoriaSerie      categoria;
    private String              sinopsis;
    private List<String>        creadores;
    private List<String>        actores;
    private List<TemporadaDTO>  temporadas;
    private EstadoSerie         estado;

    public static SerieDTO resumen(Serie s) {
        SerieDTO dto = new SerieDTO();
        dto.titulo    = s.getTitulo();
        dto.categoria = s.getCategoria();
        return dto;
    }

    public static SerieDTO detalle(Serie s) {
        SerieDTO dto = resumen(s);
        dto.sinopsis   = s.getSinopsis();
        dto.creadores  = s.getCreadores().stream().map(p -> p.getNombreCompleto()).toList();
        dto.actores    = s.getActores().stream().map(p -> p.getNombreCompleto()).toList();
        dto.temporadas = s.getTemporadas().values().stream().map(TemporadaDTO::de).toList();
        return dto;
    }

    /** Estado de serie en el espacio personal: NO incluye categoria. */
    public static SerieDTO conEstado(Serie s, EstadoSerie estado, List<TemporadaDTO> temps) {
        SerieDTO dto = new SerieDTO();
        dto.titulo     = s.getTitulo();
        dto.estado     = estado;
        dto.temporadas = temps;
        return dto;
    }
}
