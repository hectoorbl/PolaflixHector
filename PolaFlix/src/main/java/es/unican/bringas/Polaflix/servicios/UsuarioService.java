package es.unican.bringas.Polaflix.servicios;

import es.unican.bringas.Polaflix.dominio.*;
import es.unican.bringas.Polaflix.dominio.dto.*;
import es.unican.bringas.Polaflix.repositorios.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final SerieService      serieService;

    public UsuarioService(UsuarioRepository usuarioRepo, SerieService serieService) {
        this.usuarioRepo  = usuarioRepo;
        this.serieService = serieService;
    }

    /* -------------------- AUTH -------------------- */

    @Transactional
    public ResponseEntity<Void> signup(UsuarioDTO req) {
        if (req == null || req.getNombreUsuario() == null || req.getNombreUsuario().isBlank())
            return ResponseEntity.badRequest().build();

        if (usuarioRepo.existsByNombreUsuario(req.getNombreUsuario()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        Usuario u = new Usuario(
            req.getNombreUsuario(),
            req.getContrasena() == null ? "" : req.getContrasena(),
            req.getIban()       == null ? "" : req.getIban(),
            req.getTipoTarifa() == null ? TipoTarifa.POR_CAPITULO : req.getTipoTarifa()
        );
        usuarioRepo.save(u);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header("Location", "/usuarios/" + u.getNombreUsuario())
            .build();
    }

    public ResponseEntity<UsuarioDTO> login(UsuarioDTO req) {
        if (req == null || req.getNombreUsuario() == null || req.getContrasena() == null)
            return ResponseEntity.badRequest().build();

        Optional<Usuario> opt = usuarioRepo
            .findByNombreUsuarioAndContrasena(req.getNombreUsuario(), req.getContrasena());
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Usuario u = opt.get();
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(
            (u.getNombreUsuario() + ":" + UUID.randomUUID()).getBytes()
        );
        return ResponseEntity.ok(UsuarioDTO.loginResponse(u.getNombreUsuario(), token));
    }

    /* -------------------- ESPACIO PERSONAL -------------------- */

    public ResponseEntity<EspacioPersonalDTO> espacioPersonal(String nombreUsuario) {
        Optional<Usuario> opt = usuarioRepo.findByNombreUsuarioWithSeries(nombreUsuario);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        List<SerieDTO> pendientes = new ArrayList<>();
        List<SerieDTO> empezadas  = new ArrayList<>();
        List<SerieDTO> terminadas = new ArrayList<>();

        for (UsuarioSerie us : opt.get().getSeries().values()) {
            SerieDTO dto = SerieDTO.resumen(us.getSerie());
            switch (us.getEstado()) {
                case PENDIENTE -> pendientes.add(dto);
                case EMPEZADA  -> empezadas.add(dto);
                case TERMINADA -> terminadas.add(dto);
            }
        }
        return ResponseEntity.ok(new EspacioPersonalDTO(pendientes, empezadas, terminadas));
    }

    @Transactional
    public ResponseEntity<Void> agregarSerie(String nombreUsuario, String tituloSerie) {
        Optional<Usuario> uOpt = usuarioRepo.findByNombreUsuario(nombreUsuario);
        if (uOpt.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Serie> sOpt = serieService.buscarPorTitulo(tituloSerie);
        if (sOpt.isEmpty()) return ResponseEntity.notFound().build();

        Usuario u = uOpt.get();
        Serie   s = sOpt.get();

        // ⚠️ Antes: u.getSeries().containsKey(s.getTitulo()) — el mapa es Map<Serie, …>,
        // así que pasarle un String siempre devolvía false. Ahora usamos el helper.
        if (u.getUsuarioSerie(s).isPresent())
            return ResponseEntity.noContent().build();

        u.agregarSerie(s);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<SerieDTO> estadoSerie(String nombreUsuario, String tituloSerie) {
        Optional<Usuario> opt = usuarioRepo.findByNombreUsuarioWithSeriesAndCapitulosVistos(nombreUsuario);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        // Necesitamos primero localizar la Serie por título para poder buscarla
        // en el Map<Serie, UsuarioSerie> del usuario.
        Optional<Serie> sOpt = serieService.buscarPorTitulo(tituloSerie);
        if (sOpt.isEmpty()) return ResponseEntity.notFound().build();
        Serie serie = sOpt.get();

        Usuario u = opt.get();
        Optional<UsuarioSerie> usOpt = u.getUsuarioSerie(serie);
        if (usOpt.isEmpty()) return ResponseEntity.notFound().build();
        UsuarioSerie us = usOpt.get();

        // Forzar carga lazy de temporadas/capítulos dentro de la transacción.
        serie.getTemporadas().values().forEach(t -> t.getCapitulos().size());

        Set<Long> vistos = new HashSet<>();
        for (CapituloVisto cv : us.getCapitulosVistos())
            vistos.add(((long) cv.getNumTemporada()) * 10_000L + cv.getNumCapitulo());

        List<TemporadaDTO> temps = serie.getTemporadas().values().stream()
            .map(t -> {
                List<CapituloDTO> caps = t.getCapitulos().values().stream()
                    .map(c -> CapituloDTO.conSoloVisto(c.getNumero(),
                        vistos.contains(((long) t.getNumero()) * 10_000L + c.getNumero())))
                    .toList();
                return TemporadaDTO.conCapitulos(t.getNumero(), caps);
            })
            .toList();

        return ResponseEntity.ok(SerieDTO.conEstado(serie, us.getEstado(), temps));
    }

    /* -------------------- VISUALIZACIÓN -------------------- */

    @Transactional
    public ResponseEntity<LineaFacturaDTO> registrarVisualizacion(
            String nombreUsuario, String tituloSerie, int numTemporada, int numCapitulo) {

        Optional<Usuario> uOpt = usuarioRepo
            .findByNombreUsuarioWithSeriesAndCapitulosVistos(nombreUsuario);
        if (uOpt.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Serie> sOpt = serieService.buscarPorTitulo(tituloSerie);
        if (sOpt.isEmpty()) return ResponseEntity.notFound().build();

        Usuario u     = uOpt.get();
        Serie   serie = sOpt.get();

        // ⚠️ Antes buscaba por título contra un Map<Serie,…>. Ahora por la Serie.
        Optional<UsuarioSerie> usOpt = u.getUsuarioSerie(serie);
        if (usOpt.isEmpty()) return ResponseEntity.notFound().build();
        UsuarioSerie us = usOpt.get();

        Optional<Capitulo> capOpt = serie.getCapitulo(numTemporada, numCapitulo);
        if (capOpt.isEmpty()) return ResponseEntity.notFound().build();
        Capitulo capitulo = capOpt.get();

        boolean yaVisto = us.getCapitulosVistos().stream()
            .anyMatch(cv -> cv.getNumTemporada() == numTemporada
                         && cv.getNumCapitulo()  == numCapitulo);
        if (yaVisto) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        LocalDate hoy = LocalDate.now();
        us.marcarCapituloVisto(numTemporada, numCapitulo, hoy, serie.totalCapitulos());
        Factura factura = u.agregarFactura(hoy.getYear(), hoy.getMonthValue());
        LineaFactura linea = factura.añadirLineaFactura(hoy, serie, numTemporada, capitulo);

        return ResponseEntity.status(HttpStatus.CREATED).body(LineaFacturaDTO.de(linea));
    }

    public Optional<Usuario> buscarPorNombre(String nombreUsuario) {
        return usuarioRepo.findByNombreUsuario(nombreUsuario);
    }
}
