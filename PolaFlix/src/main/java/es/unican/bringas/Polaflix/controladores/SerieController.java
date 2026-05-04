package es.unican.bringas.Polaflix.controladores;

import es.unican.bringas.Polaflix.dominio.dto.SerieDTO;
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
    public ResponseEntity<List<SerieDTO>> listar(
            @RequestParam(required = false) String inicial,
            @RequestParam(required = false) String query) {
        return serieService.listar(inicial, query);
    }

    @GetMapping("/{titulo}")
    public ResponseEntity<SerieDTO> detalle(@PathVariable String titulo) {
        return serieService.detalle(titulo);
    }
}
