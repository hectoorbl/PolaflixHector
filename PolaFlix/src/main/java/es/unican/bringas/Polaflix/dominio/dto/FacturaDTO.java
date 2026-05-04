package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * La API exige nombres distintos según el contexto:
 *  - Resumen (listado): {año, mes, importe}
 *  - Detalle (mensual): {año, mes, lineas, total}
 *
 * Por eso hay dos campos separados {@code importe} y {@code total}, ambos
 * {@link Double} para que {@code @JsonInclude(NON_NULL)} omita el que no
 * corresponda.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacturaDTO {

    @JsonProperty("año")
    private int                    anio;

    private int                    mes;
    private Double                 importe;        // sólo en el resumen
    private List<LineaFacturaDTO>  lineas;         // sólo en el detalle
    private Double                 total;          // sólo en el detalle

    public static FacturaDTO resumen(int anio, int mes, double importe) {
        FacturaDTO dto = new FacturaDTO();
        dto.anio    = anio;
        dto.mes     = mes;
        dto.importe = importe;
        return dto;
    }

    public static FacturaDTO detalle(int anio, int mes, double total, List<LineaFacturaDTO> lineas) {
        FacturaDTO dto = new FacturaDTO();
        dto.anio   = anio;
        dto.mes    = mes;
        dto.lineas = lineas;
        dto.total  = total;
        return dto;
    }
}
