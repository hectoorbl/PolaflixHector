package es.unican.bringas.Polaflix.controladores;

import es.unican.bringas.Polaflix.dominio.dto.EspacioPersonalDTO;
import es.unican.bringas.Polaflix.dominio.dto.LineaFacturaDTO;
import es.unican.bringas.Polaflix.dominio.dto.SerieDTO;
import es.unican.bringas.Polaflix.dominio.dto.UsuarioDTO;
import es.unican.bringas.Polaflix.servicios.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) { this.usuarioService = usuarioService; }

    /* AUTH */

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UsuarioDTO body) {
        return usuarioService.signup(body);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody UsuarioDTO body) {
        return usuarioService.login(body);
    }

    /* ESPACIO PERSONAL */

    @GetMapping("/usuarios/{u}/series")
    public ResponseEntity<EspacioPersonalDTO> espacioPersonal(@PathVariable("u") String u) {
        return usuarioService.espacioPersonal(u);
    }

    @PostMapping("/usuarios/{u}/series/{titulo}")
    public ResponseEntity<Void> agregarSerie(@PathVariable("u") String u,
                                             @PathVariable("titulo") String titulo) {
        return usuarioService.agregarSerie(u, titulo);
    }

    @GetMapping("/usuarios/{u}/series/{titulo}")
    public ResponseEntity<SerieDTO> estadoSerie(@PathVariable("u") String u,
                                                @PathVariable("titulo") String titulo) {
        return usuarioService.estadoSerie(u, titulo);
    }

    /* VISUALIZACIÓN */

    @PostMapping("/usuarios/{u}/series/{titulo}/temporadas/{t}/capitulos/{c}/visualizaciones")
    public ResponseEntity<LineaFacturaDTO> visualizar(@PathVariable("u") String u,
                                                      @PathVariable("titulo") String titulo,
                                                      @PathVariable("t") int t,
                                                      @PathVariable("c") int c) {
        return usuarioService.registrarVisualizacion(u, titulo, t, c);
    }
}
