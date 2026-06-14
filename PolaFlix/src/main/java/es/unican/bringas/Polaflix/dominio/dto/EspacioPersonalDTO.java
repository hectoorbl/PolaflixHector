package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.EstadoSerie;
import es.unican.bringas.Polaflix.dominio.Usuario;
import lombok.Getter;

import java.util.List;

@Getter
public class EspacioPersonalDTO {

    @JsonProperty("pendientes")
    private final List<SerieResumenDTO> pendientes;

    @JsonProperty("empezadas")
    private final List<SerieResumenDTO> empezadas;

    @JsonProperty("terminadas")
    private final List<SerieResumenDTO> terminadas;

    public EspacioPersonalDTO(Usuario u) {
        this.pendientes = u.obtenerSeriesConEstado(EstadoSerie.PENDIENTE).stream()
                .map(us -> new SerieResumenDTO(us.getSerie()))
                .toList();
        this.empezadas  = u.obtenerSeriesConEstado(EstadoSerie.EMPEZADA).stream()
                .map(us -> new SerieResumenDTO(us.getSerie()))
                .toList();
        this.terminadas = u.obtenerSeriesConEstado(EstadoSerie.TERMINADA).stream()
                .map(us -> new SerieResumenDTO(us.getSerie()))
                .toList();
    }
}
