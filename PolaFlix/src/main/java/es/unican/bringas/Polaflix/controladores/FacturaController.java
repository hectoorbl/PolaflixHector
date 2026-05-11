package es.unican.bringas.Polaflix.controladores;

import es.unican.bringas.Polaflix.dominio.dto.FacturaDTO;
import es.unican.bringas.Polaflix.servicios.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{u}/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) { this.facturaService = facturaService; }

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listar(@PathVariable("u") String u) {
        return facturaService.listar(u);
    }

    @GetMapping("/actual")
    public ResponseEntity<FacturaDTO> actual(@PathVariable("u") String u) {
        return facturaService.actual(u);
    }

    @GetMapping("/{anio}/{mes}")
    public ResponseEntity<FacturaDTO> detalle(@PathVariable("u") String u,
                                              @PathVariable("anio") int anio,
                                              @PathVariable("mes") int mes) {
        return facturaService.obtener(u, anio, mes);
    }
}
