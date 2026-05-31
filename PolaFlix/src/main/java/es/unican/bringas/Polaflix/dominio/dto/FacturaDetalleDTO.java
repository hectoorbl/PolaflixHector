package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.Factura;
import es.unican.bringas.Polaflix.dominio.TipoTarifa;
import lombok.Getter;

import java.util.List;

@Getter
public class FacturaDetalleDTO {

    @JsonProperty("anio")
    private final int anio;

    @JsonProperty("mes")
    private final int mes;

    @JsonProperty("lineas")
    private final List<LineaFacturaDTO> lineas;

    @JsonProperty("total")
    private final double total;

    public FacturaDetalleDTO(Factura f, TipoTarifa tarifa) {
        this.anio   = f.getAnio();
        this.mes    = f.getMes();
        this.lineas = f.getLineas().stream().map(LineaFacturaDTO::new).toList();
        this.total  = f.calcularImporte(tarifa);
    }
}
