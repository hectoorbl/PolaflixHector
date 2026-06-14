package es.unican.bringas.Polaflix.controladores;

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
    public ResponseEntity<List<SerieResumenDTO>> listar(
            @RequestParam String usuario,
            @RequestParam(required = false) String inicial,
            @RequestParam(required = false) String nombre) {

        if (usuario.isBlank())
            return ResponseEntity.badRequest().build();
        if (inicial != null && nombre != null)
            return ResponseEntity.badRequest().build();

        if (nombre != null && !nombre.isBlank())
            return ResponseEntity.ok(serieService.buscarPorNombre(nombre));

        return ResponseEntity.ok(serieService.listarPorInicial(inicial));
    }

    @GetMapping("/{titulo}")
    public ResponseEntity<SerieDetalleDTO> detalle(
            @PathVariable String titulo,
            @RequestParam String usuario) {

        if (usuario.isBlank())
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(serieService.obtenerDetalle(titulo));
    }
}
