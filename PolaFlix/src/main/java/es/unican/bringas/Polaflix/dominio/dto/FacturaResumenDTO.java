package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.Factura;
import es.unican.bringas.Polaflix.dominio.TipoTarifa;
import lombok.Getter;

@Getter
public class FacturaResumenDTO {

    @JsonProperty("anio")
    private final int anio;

    @JsonProperty("mes")
    private final int mes;

    @JsonProperty("importe")
    private final double importe;

    public FacturaResumenDTO(Factura f, TipoTarifa tarifa) {
        this.anio    = f.getAnio();
        this.mes     = f.getMes();
        this.importe = f.calcularImporte(tarifa);
    }
}
