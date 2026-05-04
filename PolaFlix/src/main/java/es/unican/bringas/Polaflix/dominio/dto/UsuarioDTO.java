package es.unican.bringas.Polaflix.dominio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.unican.bringas.Polaflix.dominio.TipoTarifa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de Usuario. Sirve como cuerpo de POST /signup, cuerpo de POST /login y
 * respuesta del login. Los campos no usados quedan a null y {@code @JsonInclude}
 * los omite del JSON.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDTO {

    private String      nombreUsuario;
    private String      contrasena;

    @JsonProperty("IBAN")
    private String      iban;

    private TipoTarifa  tipoTarifa;

    public static UsuarioDTO loginResponse(String nombreUsuario, String contrasena) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.nombreUsuario = nombreUsuario;
        dto.contrasena         = contrasena;
        return dto;
    }
}
