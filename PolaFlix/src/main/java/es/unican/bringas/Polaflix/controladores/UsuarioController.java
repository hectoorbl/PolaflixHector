package es.unican.bringas.Polaflix.controladores;

import es.unican.bringas.Polaflix.dominio.TipoTarifa;
import es.unican.bringas.Polaflix.dominio.dto.EspacioPersonalDTO;
import es.unican.bringas.Polaflix.dominio.dto.LineaFacturaDTO;
import es.unican.bringas.Polaflix.dominio.dto.SerieUsuarioDTO;
import es.unican.bringas.Polaflix.servicios.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) { this.usuarioService = usuarioService; }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody Map<String, String> body) {
        String usuario = body.get("nombreUsuario");
        String pass    = body.get("contrasena");
        if (usuario == null || usuario.isBlank() || pass == null || pass.isBlank())
            return ResponseEntity.badRequest().build();

        TipoTarifa tarifa = null;
        if (body.containsKey("tipoTarifa")) {
            try { tarifa = TipoTarifa.valueOf(body.get("tipoTarifa")); }
            catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
        }

        usuarioService.registrarUsuario(usuario, pass, body.get("IBAN"), tarifa);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/usuarios/" + usuario)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Map<String, String> body) {
        String usuario = body.get("nombreUsuario");
        String pass    = body.get("contrasena");
        if (usuario == null || usuario.isBlank() || pass == null || pass.isBlank())
            return ResponseEntity.badRequest().build();

        usuarioService.autenticarUsuario(usuario, pass);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuarios/{u}/series")
    public ResponseEntity<EspacioPersonalDTO> espacioPersonal(@PathVariable("u") String u) {
        return ResponseEntity.ok(usuarioService.obtenerEspacioPersonal(u));
    }

    @PostMapping("/usuarios/{u}/series/{titulo}")
    public ResponseEntity<Void> agregarSerie(@PathVariable("u") String u,
                                             @PathVariable("titulo") String titulo) {
        usuarioService.agregarSerie(u, titulo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/usuarios/" + u + "/series/" + titulo)
                .build();
    }

    @GetMapping("/usuarios/{u}/series/{titulo}")
    public ResponseEntity<SerieUsuarioDTO> estadoSerie(@PathVariable("u") String u,
                                                       @PathVariable("titulo") String titulo) {
        return ResponseEntity.ok(usuarioService.obtenerEstadoSerie(u, titulo));
    }

    @PostMapping("/usuarios/{u}/series/{titulo}/temporadas/{t}/capitulos/{c}/visualizaciones")
    public ResponseEntity<LineaFacturaDTO> visualizar(@PathVariable("u") String u,
                                                      @PathVariable("titulo") String titulo,
                                                      @PathVariable("t") int t,
                                                      @PathVariable("c") int c) {
        LineaFacturaDTO linea = usuarioService.registrarVisualizacion(u, titulo, t, c);
        return ResponseEntity.status(HttpStatus.CREATED).body(linea);
    }
}
