package es.unican.bringas.Polaflix.servicios;

import es.unican.bringas.Polaflix.dominio.*;
import es.unican.bringas.Polaflix.dominio.dto.*;
import es.unican.bringas.Polaflix.repositorios.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final SerieService      serieService;

    public UsuarioService(UsuarioRepository usuarioRepo, SerieService serieService) {
        this.usuarioRepo  = usuarioRepo;
        this.serieService = serieService;
    }

    @Transactional
    public void registrarUsuario(String nombreUsuario, String contrasena, String iban, TipoTarifa tarifa) {
        if (usuarioRepo.existsByNombreUsuario(nombreUsuario))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya existe: " + nombreUsuario);
        usuarioRepo.save(new Usuario(
                nombreUsuario,
                contrasena,
                iban   == null ? "" : iban,
                tarifa == null ? TipoTarifa.POR_CAPITULO : tarifa));
    }

    public void autenticarUsuario(String nombreUsuario, String contrasena) {
        if (!usuarioRepo.existsByNombreUsuario(nombreUsuario))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + nombreUsuario);
        usuarioRepo.findByNombreUsuarioAndContrasena(nombreUsuario, contrasena)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Nombre de usuario o contraseña incorrectos"));
    }

    public EspacioPersonalDTO obtenerEspacioPersonal(String nombreUsuario) {
        Usuario u = usuarioRepo.findByNombreUsuarioWithSeriesAndCapitulosVistos(nombreUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + nombreUsuario));
        return new EspacioPersonalDTO(u);
    }

    @Transactional
    public void agregarSerie(String nombreUsuario, String tituloSerie) {
        Usuario u = usuarioRepo.findByNombreUsuarioWithSeries(nombreUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + nombreUsuario));
        Serie s = serieService.buscarSerie(tituloSerie);

        if (u.tieneSerie(s))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La serie ya está agregada: " + tituloSerie);

        u.agregarSerie(s);
    }

    public SerieUsuarioDTO obtenerEstadoSerie(String nombreUsuario, String tituloSerie) {
        Usuario u = usuarioRepo.findByNombreUsuarioWithSeriesAndCapitulosVistos(nombreUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + nombreUsuario));
        Serie s = serieService.buscarSerie(tituloSerie);

        UsuarioSerie us = u.getUsuarioSerie(s)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "La serie no está en el espacio personal del usuario"));

        s.getTemporadas().values().forEach(t -> t.getCapitulos().size());

        return new SerieUsuarioDTO(s, us);
    }

    @Transactional
    public LineaFacturaDTO registrarVisualizacion(String nombreUsuario, String tituloSerie,
                                                  int numTemporada, int numCapitulo) {
        Usuario u = usuarioRepo.findByNombreUsuarioWithSeriesAndCapitulosVistos(nombreUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + nombreUsuario));
        Serie s = serieService.buscarSerie(tituloSerie);

        u.getUsuarioSerie(s)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "La serie no está en el espacio personal del usuario"));

        Capitulo c = s.getCapitulo(numTemporada, numCapitulo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Capítulo no encontrado: T" + numTemporada + "C" + numCapitulo));

        LineaFactura linea = u.visualizarCapitulo(s, numTemporada, c);
        return new LineaFacturaDTO(linea);
    }

    public Usuario buscarUsuario(String nombreUsuario) {
        return usuarioRepo.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + nombreUsuario));
    }
}
