package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.unican.bringas.Polaflix.dominio.Capitulo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  - {@link #de}            → {numero, titulo, descripcion}  (catálogo)
 *  - {@link #conSoloVisto}  → {numero, visto}                (estado de serie del usuario)
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CapituloDTO {

    private int     numero;
    private String  titulo;
    private String  descripcion;
    private Boolean visto;

    /** Capítulo del catálogo con título y descripción. */
    public static CapituloDTO de(Capitulo c) {
        CapituloDTO dto = new CapituloDTO();
        dto.numero      = c.getNumero();
        dto.titulo      = c.getTitulo();
        dto.descripcion = c.getDescripcion();
        return dto;
    }

    /** Capítulo del estado de serie: sólo número y flag de visualización. */
    public static CapituloDTO conSoloVisto(int numero, boolean visto) {
        CapituloDTO dto = new CapituloDTO();
        dto.numero = numero;
        dto.visto  = visto;
        return dto;
    }
}
