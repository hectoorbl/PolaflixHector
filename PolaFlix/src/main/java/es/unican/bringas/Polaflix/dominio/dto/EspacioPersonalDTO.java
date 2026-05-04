package es.unican.bringas.Polaflix.dominio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Espacio personal del usuario, agrupado por estado de cada serie tal y como
 * exige la API. Cada elemento de las listas es un {@link SerieDTO} en su forma
 * de resumen ({titulo, categoria}).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EspacioPersonalDTO {

    private List<SerieDTO> pendientes;
    private List<SerieDTO> empezadas;
    private List<SerieDTO> terminadas;
}
