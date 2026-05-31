package es.unican.bringas.Polaflix.controladores;

import es.unican.bringas.Polaflix.dominio.dto.SerieBuscadaDTO;
import es.unican.bringas.Polaflix.dominio.dto.SerieDetalleDTO;
import es.unican.bringas.Polaflix.dominio.dto.SerieResumenDTO;
import es.unican.bringas.Polaflix.servicios.SerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    private final SerieService serieService;

    public SerieController(SerieService serieService) { this.serieService = serieService; }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam String usuario,
            @RequestParam(required = false) String inicial,
            @RequestParam(required = false) String nombre) {

        if (usuario.isBlank())
            return ResponseEntity.badRequest().body("El parámetro 'usuario' es obligatorio");
        if (inicial != null && nombre != null)
            return ResponseEntity.badRequest().body("'inicial' y 'nombre' son excluyentes");

        if (nombre != null && !nombre.isBlank()) {
            List<SerieBuscadaDTO> resultado = serieService.buscarPorNombre(usuario, nombre);
            return ResponseEntity.ok(resultado);
        }

        List<SerieResumenDTO> resultado = serieService.listarPorInicial(usuario, inicial);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{titulo}")
    public ResponseEntity<SerieDetalleDTO> detalle(
            @PathVariable String titulo,
            @RequestParam String usuario) {

        if (usuario.isBlank())
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(serieService.obtenerDetalle(titulo, usuario));
    }
}
