package es.unican.bringas.Polaflix.servicios;

import es.unican.bringas.Polaflix.dominio.Factura;
import es.unican.bringas.Polaflix.dominio.Usuario;
import es.unican.bringas.Polaflix.dominio.dto.FacturaDetalleDTO;
import es.unican.bringas.Polaflix.dominio.dto.FacturaResumenDTO;
import es.unican.bringas.Polaflix.repositorios.FacturaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FacturaService {

    private final FacturaRepository facturaRepo;
    private final UsuarioService    usuarioService;

    public FacturaService(FacturaRepository facturaRepo, UsuarioService usuarioService) {
        this.facturaRepo    = facturaRepo;
        this.usuarioService = usuarioService;
    }

    public List<FacturaResumenDTO> listarFacturas(String nombreUsuario) {
        Usuario u = usuarioService.buscarUsuario(nombreUsuario);
        return facturaRepo.findByUsuarioOrderByAnioDescMesDesc(u).stream()
                .map(f -> new FacturaResumenDTO(f, u.getTarifa()))
                .toList();
    }

    public FacturaDetalleDTO obtenerFactura(String nombreUsuario, int anio, int mes) {
        Usuario u = usuarioService.buscarUsuario(nombreUsuario);
        Factura f = facturaRepo.findByUsuarioAndMesAndAnioWithLineas(u, mes, anio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe factura para " + anio + "/" + mes));
        return new FacturaDetalleDTO(f, u.getTarifa());
    }

    public FacturaDetalleDTO obtenerFacturaActual(String nombreUsuario) {
        LocalDate hoy = LocalDate.now();
        return obtenerFactura(nombreUsuario, hoy.getYear(), hoy.getMonthValue());
    }
}
