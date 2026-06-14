package es.unican.bringas.Polaflix.controladores;

import es.unican.bringas.Polaflix.dominio.dto.FacturaDTO;
import es.unican.bringas.Polaflix.servicios.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{u}/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) { this.facturaService = facturaService; }

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listar(@PathVariable("u") String u) {
        return ResponseEntity.ok(facturaService.listarFacturas(u));
    }

    @GetMapping("/actual")
    public ResponseEntity<FacturaDTO> actual(@PathVariable("u") String u) {
        return ResponseEntity.ok(facturaService.obtenerFacturaActual(u));
    }

    @GetMapping("/{anio}/{mes}")
    public ResponseEntity<FacturaDTO> detalle(@PathVariable("u") String u,
                                              @PathVariable int anio,
                                              @PathVariable int mes) {
        if (mes < 1 || mes > 12 || anio < 1)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(facturaService.obtenerFactura(u, anio, mes));
    }
}
