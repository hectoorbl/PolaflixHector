package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.LineaFactura;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LineaFacturaDTO {

    @JsonProperty("fecha")
    private final LocalDate fecha;

    @JsonProperty("serie")
    private final String serie;

    @JsonProperty("temporada")
    private final int temporada;

    @JsonProperty("capitulo")
    private final int capitulo;

    @JsonProperty("titulo")
    private final String titulo;

    @JsonProperty("cargo")
    private final double cargo;

    public LineaFacturaDTO(LineaFactura l) {
        this.fecha     = l.getFechaVisualizacion();
        this.serie     = l.getTituloSerie();
        this.temporada = l.getNumeroTemporada();
        this.capitulo  = l.getNumeroCapitulo();
        this.titulo    = l.getTituloCapitulo();
        this.cargo     = l.getCargo();
    }
}
