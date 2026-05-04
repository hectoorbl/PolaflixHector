package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.unican.bringas.Polaflix.dominio.LineaFactura;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LineaFacturaDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate  fecha;

    private String     serie;
    private int        temporada;
    private int        capitulo;
    private double     cargo;

    public static LineaFacturaDTO de(LineaFactura lf) {
        LineaFacturaDTO dto = new LineaFacturaDTO();
        dto.fecha     = lf.getFechaVisualizacion();
        dto.serie     = lf.getTituloSerie();
        dto.temporada = lf.getNumeroTemporada();
        dto.capitulo  = lf.getNumeroCapitulo();
        dto.cargo     = lf.getCargo();
        return dto;
    }
}
