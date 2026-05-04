package es.unican.bringas.Polaflix.servicios;

import es.unican.bringas.Polaflix.dominio.Factura;
import es.unican.bringas.Polaflix.dominio.Usuario;
import es.unican.bringas.Polaflix.dominio.dto.FacturaDTO;
import es.unican.bringas.Polaflix.dominio.dto.LineaFacturaDTO;
import es.unican.bringas.Polaflix.repositorios.FacturaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class FacturaService {

    private final FacturaRepository facturaRepo;
    private final UsuarioService    usuarioService;

    public FacturaService(FacturaRepository facturaRepo, UsuarioService usuarioService) {
        this.facturaRepo    = facturaRepo;
        this.usuarioService = usuarioService;
    }

    /** GET /usuarios/{u}/facturas → listado, más reciente primero. Cada item: {año, mes, importe}. */
    public ResponseEntity<List<FacturaDTO>> listar(String nombreUsuario) {
        Optional<Usuario> opt = usuarioService.buscarPorNombre(nombreUsuario);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Usuario u = opt.get();
        List<FacturaDTO> facturas = facturaRepo.findByUsuarioOrderByAnioDescMesDesc(u).stream()
            .map(f -> FacturaDTO.resumen(f.getAnio(), f.getMes(), f.calcularImporte(u.getTarifa())))
            .toList();
        return ResponseEntity.ok(facturas);
    }

    /** GET /usuarios/{u}/facturas/{anio}/{mes} → detalle: {año, mes, lineas, total}. */
    public ResponseEntity<FacturaDTO> obtener(String nombreUsuario, int anio, int mes) {
        if (mes < 1 || mes > 12 || anio < 1)
            return ResponseEntity.badRequest().build();

        Optional<Usuario> uOpt = usuarioService.buscarPorNombre(nombreUsuario);
        if (uOpt.isEmpty()) return ResponseEntity.notFound().build();
        Usuario u = uOpt.get();

        Optional<Factura> fOpt = facturaRepo.findByUsuarioAndMesAndAnioWithLineas(u, mes, anio);
        if (fOpt.isEmpty()) return ResponseEntity.notFound().build();
        Factura f = fOpt.get();

        List<LineaFacturaDTO> lineas = f.getLineas().stream()
            .map(LineaFacturaDTO::de).toList();

        return ResponseEntity.ok(FacturaDTO.detalle(
            f.getAnio(), f.getMes(), f.calcularImporte(u.getTarifa()), lineas));
    }

    /** GET /usuarios/{u}/facturas/actual → factura del mes en curso. */
    public ResponseEntity<FacturaDTO> actual(String nombreUsuario) {
        LocalDate hoy = LocalDate.now();
        return obtener(nombreUsuario, hoy.getYear(), hoy.getMonthValue());
    }
}
