package es.unican.bringas.Polaflix.servicios;

import es.unican.bringas.Polaflix.dominio.*;
import es.unican.bringas.Polaflix.dominio.dto.*;
import es.unican.bringas.Polaflix.excepciones.ConflictoException;
import es.unican.bringas.Polaflix.excepciones.CredencialesInvalidasException;
import es.unican.bringas.Polaflix.excepciones.RecursoNoEncontradoException;
import es.unican.bringas.Polaflix.repositorios.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.UUID;

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
            throw new ConflictoException("El nombre de usuario ya existe: " + nombreUsuario);
        usuarioRepo.save(new Usuario(
                nombreUsuario,
                contrasena,
                iban   == null ? "" : iban,
                tarifa == null ? TipoTarifa.POR_CAPITULO : tarifa));
    }

    public String autenticarUsuario(String nombreUsuario, String contrasena) {
        if (!usuarioRepo.existsByNombreUsuario(nombreUsuario))
            throw new RecursoNoEncontradoException("Usuario no encontrado: " + nombreUsuario);
        usuarioRepo.findByNombreUsuarioAndContrasena(nombreUsuario, contrasena)
                .orElseThrow(() -> new CredencialesInvalidasException("Contraseña incorrecta"));
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString((nombreUsuario + ":" + UUID.randomUUID()).getBytes());
    }

    public EspacioPersonalDTO obtenerEspacioPersonal(String nombreUsuario) {
        Usuario u = usuarioRepo.findByNombreUsuarioWithSeriesAndCapitulosVistos(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + nombreUsuario));
        return new EspacioPersonalDTO(u);
    }

    @Transactional
    public void agregarSerie(String nombreUsuario, String tituloSerie) {
        Usuario u = usuarioRepo.findByNombreUsuarioWithSeries(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + nombreUsuario));
        Serie s = serieService.buscarSerie(tituloSerie);

        if (u.tieneSerie(s))
            throw new ConflictoException("La serie ya está agregada: " + tituloSerie);

        u.agregarSerie(s);
    }

    public SerieUsuarioDTO obtenerEstadoSerie(String nombreUsuario, String tituloSerie) {
        Usuario u = usuarioRepo.findByNombreUsuarioWithSeriesAndCapitulosVistos(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + nombreUsuario));
        Serie s = serieService.buscarSerie(tituloSerie);

        UsuarioSerie us = u.getUsuarioSerie(s)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La serie no está en el espacio personal del usuario"));

        s.getTemporadas().values().forEach(t -> t.getCapitulos().size());

        return new SerieUsuarioDTO(s, us);
    }

    @Transactional
    public LineaFacturaDTO registrarVisualizacion(String nombreUsuario, String tituloSerie,
                                                  int numTemporada, int numCapitulo) {
        Usuario u = usuarioRepo.findByNombreUsuarioWithSeriesAndCapitulosVistos(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + nombreUsuario));
        Serie s = serieService.buscarSerie(tituloSerie);

        u.getUsuarioSerie(s)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La serie no está en el espacio personal del usuario"));

        Capitulo c = s.getCapitulo(numTemporada, numCapitulo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Capítulo no encontrado: T" + numTemporada + "C" + numCapitulo));

        LineaFactura linea = u.visualizarCapitulo(s, numTemporada, c);
        return new LineaFacturaDTO(linea);
    }

    public Usuario buscarUsuario(String nombreUsuario) {
        return usuarioRepo.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + nombreUsuario));
    }
}
