package es.unican.bringas.Polaflix;

import es.unican.bringas.Polaflix.dominio.*;
import es.unican.bringas.Polaflix.repositorios.FacturaRepository;
import es.unican.bringas.Polaflix.repositorios.SerieRepository;
import es.unican.bringas.Polaflix.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Carga inicial extensa para H2.
 *
 *  - 15 series cubriendo distintas iniciales del alfabeto (para probar el
 *    filtro por inicial del catálogo) y las 3 categorías.
 *  - Temporadas y capítulos con suficiente volumen para que las facturas
 *    tengan líneas variadas.
 *  - 4 usuarios: 3 con tarifa POR_CAPITULO y 1 con tarifa PLANA, cubriendo
 *    los 3 estados de UsuarioSerie y casos sin actividad.
 */
@Component
public class AppFeeder implements CommandLineRunner {

	@Autowired
	protected UsuarioRepository ur;
	@Autowired
	protected SerieRepository   sr;
	@Autowired
	protected FacturaRepository fr;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if (sr.count() > 0 || ur.count() > 0) {
			System.out.println("[AppFeeder] BBDD ya contiene datos, no se carga nada.");
			return;
		}

		feedSeries();
		feedUsuarios();
		feedVisualizaciones();

		testRepositorios();

		System.out.println("Application feeded");
	}

	/* ============================================================== */
	/*  CATÁLOGO DE SERIES                                             */
	/* ============================================================== */

	private void feedSeries() {

		feedBreakingBad();
		feedBetterCallSaul();
		feedChernobyl();
		feedDexter();
		feedFriends();
		feedJuegoDeTronos();
		feedHouse();
		feedLost();
		feedLaCasaDePapel();
		feedLosSerrano();
		feedMrRobot();
		feedNarcos();
		feedPeakyBlinders();
		feedStrangerThings();
		feedTheWire();

		System.out.println("[AppFeeder] " + sr.count() + " series cargadas.");
	}

	/* ---------------------------------------------------------------- */
	/*  Cada serie en su propio método para mantener el feeder legible. */
	/* ---------------------------------------------------------------- */

	private void feedBreakingBad() {
		Serie s = new Serie("Breaking Bad",
				"Un profesor de química con cáncer terminal se convierte en fabricante "
						+ "de metanfetamina para asegurar el futuro económico de su familia.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("Vince", "Gilligan"));
		s.addActor(new Persona("Bryan",   "Cranston"));
		s.addActor(new Persona("Aaron",   "Paul"));
		s.addActor(new Persona("Anna",    "Gunn"));
		s.addActor(new Persona("Dean",    "Norris"));
		s.addActor(new Persona("Bob",     "Odenkirk"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Pilot",
				"Walter White recibe un diagnóstico devastador y toma una decisión radical."));
		t1.addCapitulo(new Capitulo(2, "Cat's in the Bag...",
				"Walter y Jesse intentan deshacerse de los cuerpos."));
		t1.addCapitulo(new Capitulo(3, "...And the Bag's in the River",
				"Walter se enfrenta a una decisión imposible."));
		t1.addCapitulo(new Capitulo(4, "Cancer Man",
				"La familia White conoce el verdadero estado de Walter."));
		t1.addCapitulo(new Capitulo(5, "Gray Matter",
				"Walter rechaza ayuda económica de viejos amigos."));
		t1.addCapitulo(new Capitulo(6, "Crazy Handful of Nothin'",
				"Walter adopta una identidad alternativa y se enfrenta a Tuco."));
		t1.addCapitulo(new Capitulo(7, "A No-Rough-Stuff-Type Deal",
				"Walter y Jesse aceptan un nuevo trato peligroso con Tuco."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "Seven Thirty-Seven",
				"Walter y Jesse calculan cuánto necesitan ganar para retirarse."));
		t2.addCapitulo(new Capitulo(2, "Grilled",
				"Tuco lleva a Walter y Jesse al desierto."));
		t2.addCapitulo(new Capitulo(3, "Bit by a Dead Bee",
				"Walter inventa una excusa para volver a casa."));
		t2.addCapitulo(new Capitulo(4, "Down",
				"Jesse pierde su casa y Walter intenta reconectar con su familia."));
		t2.addCapitulo(new Capitulo(5, "Breakage",
				"El equipo de distribución de Jesse comienza a tener problemas."));
		t2.addCapitulo(new Capitulo(6, "Peekaboo",
				"Jesse confronta a unos drogadictos que le robaron."));
		s.addTemporada(t2);

		Temporada t3 = new Temporada(3);
		t3.addCapitulo(new Capitulo(1, "No Más",
				"Walter intenta reconciliarse con Skyler tras la confesión."));
		t3.addCapitulo(new Capitulo(2, "Caballo Sin Nombre",
				"Walter es perseguido por dos misteriosos primos."));
		t3.addCapitulo(new Capitulo(3, "I.F.T.",
				"La relación entre Walter y Skyler se complica aún más."));
		t3.addCapitulo(new Capitulo(4, "Green Light",
				"Walter pierde el control en su trabajo como profesor."));
		t3.addCapitulo(new Capitulo(5, "Más",
				"Gus Fring le ofrece a Walter un trato millonario."));
		s.addTemporada(t3);

		sr.save(s);
	}

	private void feedBetterCallSaul() {
		Serie s = new Serie("Better Call Saul",
				"La historia del abogado Jimmy McGill seis años antes de los eventos "
						+ "de Breaking Bad: cómo se convirtió en el famoso Saul Goodman.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("Vince",  "Gilligan"));
		s.addCreador(new Persona("Peter",  "Gould"));
		s.addActor(new Persona("Bob",      "Odenkirk"));
		s.addActor(new Persona("Rhea",     "Seehorn"));
		s.addActor(new Persona("Jonathan", "Banks"));
		s.addActor(new Persona("Giancarlo","Esposito"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Uno",
				"Jimmy McGill intenta hacer despegar su nuevo bufete de abogados."));
		t1.addCapitulo(new Capitulo(2, "Mijo",
				"Jimmy se enfrenta a un peligroso narcotraficante."));
		t1.addCapitulo(new Capitulo(3, "Nacho",
				"Jimmy entra en territorio peligroso por un caso."));
		t1.addCapitulo(new Capitulo(4, "Hero",
				"Jimmy llama la atención mediática con un caso espectacular."));
		t1.addCapitulo(new Capitulo(5, "Alpine Shepherd Boy",
				"Jimmy descubre un nicho legal en derecho de la tercera edad."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "Switch",
				"Jimmy comienza un nuevo trabajo en un bufete corporativo."));
		t2.addCapitulo(new Capitulo(2, "Cobbler",
				"Jimmy ayuda a Pryce con un nuevo problema."));
		t2.addCapitulo(new Capitulo(3, "Amarillo",
				"Jimmy idea una agresiva campaña publicitaria."));
		t2.addCapitulo(new Capitulo(4, "Gloves Off",
				"Mike acepta un trabajo arriesgado pero lo planifica con cuidado."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedChernobyl() {
		Serie s = new Serie("Chernobyl",
				"Recreación dramática del desastre nuclear de Chernobyl en 1986 y "
						+ "los esfuerzos por contenerlo.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("Craig",  "Mazin"));
		s.addActor(new Persona("Jared",    "Harris"));
		s.addActor(new Persona("Stellan",  "Skarsgård"));
		s.addActor(new Persona("Emily",    "Watson"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "1:23:45",
				"La noche de la explosión en el reactor cuatro."));
		t1.addCapitulo(new Capitulo(2, "Please Remain Calm",
				"Las autoridades soviéticas tratan de minimizar el desastre."));
		t1.addCapitulo(new Capitulo(3, "Open Wide, O Earth",
				"Comienza la operación de limpieza con miles de soldados."));
		t1.addCapitulo(new Capitulo(4, "The Happiness of All Mankind",
				"Equipos de élite trabajan en la limpieza del tejado del reactor."));
		t1.addCapitulo(new Capitulo(5, "Vichnaya Pamyat",
				"El juicio revela las verdaderas causas del accidente."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedDexter() {
		Serie s = new Serie("Dexter",
				"Un analista forense de Miami que también es un asesino en serie con "
						+ "un código moral propio: sólo mata a otros asesinos.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("James",  "Manos Jr."));
		s.addActor(new Persona("Michael",  "C. Hall"));
		s.addActor(new Persona("Jennifer", "Carpenter"));
		s.addActor(new Persona("David",    "Zayas"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Dexter",
				"Conocemos a Dexter Morgan, analista forense y asesino en serie."));
		t1.addCapitulo(new Capitulo(2, "Crocodile",
				"Dexter intenta atrapar a un asesino especialmente brutal."));
		t1.addCapitulo(new Capitulo(3, "Popping Cherry",
				"La investigación del Asesino del Camión Frigorífico se intensifica."));
		t1.addCapitulo(new Capitulo(4, "Let's Give the Boy a Hand",
				"Dexter recibe un mensaje muy personal del Camión Frigorífico."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "It's Alive!",
				"Dexter intenta volver a su rutina tras los eventos del año anterior."));
		t2.addCapitulo(new Capitulo(2, "Waiting to Exhale",
				"Aparece evidencia del Carnicero de Bay Harbor."));
		t2.addCapitulo(new Capitulo(3, "An Inconvenient Lie",
				"Dexter conoce a Lila, una nueva influencia peligrosa."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedFriends() {
		Serie s = new Serie("Friends",
				"Las aventuras y desventuras de seis amigos que viven en Nueva York.",
				CategoriaSerie.ESTANDAR);
		s.addCreador(new Persona("David",     "Crane"));
		s.addCreador(new Persona("Marta",     "Kauffman"));
		s.addActor(new Persona("Jennifer",    "Aniston"));
		s.addActor(new Persona("Courteney",   "Cox"));
		s.addActor(new Persona("Lisa",        "Kudrow"));
		s.addActor(new Persona("Matt",        "LeBlanc"));
		s.addActor(new Persona("Matthew",     "Perry"));
		s.addActor(new Persona("David",       "Schwimmer"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "The One Where Monica Gets a Roommate",
				"Rachel huye de su boda y Monica le ofrece quedarse en su piso."));
		t1.addCapitulo(new Capitulo(2, "The One with the Sonogram at the End",
				"Ross descubre que su exmujer está embarazada."));
		t1.addCapitulo(new Capitulo(3, "The One with the Thumb",
				"Chandler vuelve a fumar y Phoebe encuentra un pulgar en su refresco."));
		t1.addCapitulo(new Capitulo(4, "The One with George Stephanopoulos",
				"Las chicas tienen una noche de pizzas y vino."));
		t1.addCapitulo(new Capitulo(5, "The One with the East German Laundry Detergent",
				"Ross enseña a Rachel a lavar la ropa."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "The One with Ross's New Girlfriend",
				"Ross vuelve de China con una nueva novia, Julie."));
		t2.addCapitulo(new Capitulo(2, "The One with the Breast Milk",
				"Joey y Chandler descubren la leche materna."));
		t2.addCapitulo(new Capitulo(3, "The One Where Heckles Dies",
				"Muere el vecino del piso de abajo."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedJuegoDeTronos() {
		Serie s = new Serie("Juego de Tronos",
				"Nobles familias luchan por el control del Trono de Hierro de Poniente "
						+ "mientras una amenaza ancestral resurge en el norte.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("David", "Benioff"));
		s.addCreador(new Persona("D. B.", "Weiss"));
		s.addActor(new Persona("Emilia",  "Clarke"));
		s.addActor(new Persona("Kit",     "Harington"));
		s.addActor(new Persona("Peter",   "Dinklage"));
		s.addActor(new Persona("Lena",    "Headey"));
		s.addActor(new Persona("Sean",    "Bean"));
		s.addActor(new Persona("Maisie",  "Williams"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Se acerca el invierno",
				"Lord Eddard Stark recibe la visita del rey Robert."));
		t1.addCapitulo(new Capitulo(2, "El Camino Real",
				"La familia Stark se separa para servir al rey."));
		t1.addCapitulo(new Capitulo(3, "Lord Nieve",
				"Jon Nieve llega al Muro y conoce a sus compañeros."));
		t1.addCapitulo(new Capitulo(4, "Tullidos, bastardos y cosas rotas",
				"Tyrion Lannister visita el Muro de camino a Desembarco del Rey."));
		t1.addCapitulo(new Capitulo(5, "El lobo y el león",
				"Catelyn captura a Tyrion bajo sospechas."));
		t1.addCapitulo(new Capitulo(6, "Una corona de oro",
				"Daenerys come un corazón crudo en una ceremonia dothraki."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "El Norte recuerda",
				"La guerra de los Cinco Reyes comienza."));
		t2.addCapitulo(new Capitulo(2, "Las tierras de la noche",
				"Daenerys busca refugio para sus dragones recién nacidos."));
		t2.addCapitulo(new Capitulo(3, "Lo que está muerto no puede morir",
				"Theon vuelve con su familia en las Islas del Hierro."));
		t2.addCapitulo(new Capitulo(4, "Jardín de huesos",
				"Joffrey muestra su crueldad en la corte."));
		s.addTemporada(t2);

		Temporada t3 = new Temporada(3);
		t3.addCapitulo(new Capitulo(1, "Valar Dohaeris",
				"Jon es presentado a Mance Rayder, el Rey-más-allá-del-Muro."));
		t3.addCapitulo(new Capitulo(2, "Alas Negras, Palabras Negras",
				"Robb recibe noticias dolorosas sobre Invernalia."));
		t3.addCapitulo(new Capitulo(3, "Camino de Tormentas",
				"Jaime y Brienne continúan su camino hacia Desembarco del Rey."));
		s.addTemporada(t3);

		sr.save(s);
	}

	private void feedHouse() {
		Serie s = new Serie("House",
				"Un médico genio pero antisocial diagnostica casos imposibles "
						+ "junto a su equipo en el Princeton-Plainsboro Teaching Hospital.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("David",   "Shore"));
		s.addActor(new Persona("Hugh",      "Laurie"));
		s.addActor(new Persona("Lisa",      "Edelstein"));
		s.addActor(new Persona("Robert",    "Sean Leonard"));
		s.addActor(new Persona("Omar",      "Epps"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Pilot",
				"House intenta diagnosticar a una maestra de jardín de infancia."));
		t1.addCapitulo(new Capitulo(2, "Paternity",
				"House investiga el caso de un adolescente con visión doble."));
		t1.addCapitulo(new Capitulo(3, "Occam's Razor",
				"Un joven sano colapsa repentinamente en circunstancias misteriosas."));
		t1.addCapitulo(new Capitulo(4, "Maternity",
				"Una epidemia desconocida afecta a recién nacidos."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedLost() {
		Serie s = new Serie("Lost",
				"Los supervivientes de un accidente aéreo descubren que la isla en la "
						+ "que han caído es mucho más misteriosa de lo que parece.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("J. J.",    "Abrams"));
		s.addCreador(new Persona("Damon",    "Lindelof"));
		s.addCreador(new Persona("Jeffrey",  "Lieber"));
		s.addActor(new Persona("Matthew",    "Fox"));
		s.addActor(new Persona("Evangeline", "Lilly"));
		s.addActor(new Persona("Josh",       "Holloway"));
		s.addActor(new Persona("Terry",      "O'Quinn"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Pilot, Part 1",
				"El vuelo 815 se estrella en una isla aparentemente desierta."));
		t1.addCapitulo(new Capitulo(2, "Pilot, Part 2",
				"Los supervivientes oyen extraños ruidos en la jungla."));
		t1.addCapitulo(new Capitulo(3, "Tabula Rasa",
				"Se descubre el oscuro pasado de Kate."));
		t1.addCapitulo(new Capitulo(4, "Walkabout",
				"Locke organiza una partida de caza en la isla."));
		t1.addCapitulo(new Capitulo(5, "White Rabbit",
				"Jack persigue una visión de su padre por la jungla."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "Man of Science, Man of Faith",
				"El interior de la escotilla revela un nuevo misterio."));
		t2.addCapitulo(new Capitulo(2, "Adrift",
				"Michael y Sawyer luchan por sobrevivir en el océano."));
		t2.addCapitulo(new Capitulo(3, "Orientation",
				"Los supervivientes descubren un viejo vídeo en la escotilla."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedLaCasaDePapel() {
		Serie s = new Serie("La Casa de Papel",
				"Un grupo de atracadores liderados por El Profesor planea el mayor "
						+ "atraco de la historia: asaltar la Fábrica Nacional de Moneda y Timbre.",
				CategoriaSerie.ESTANDAR);
		s.addCreador(new Persona("Álex", "Pina"));
		s.addActor(new Persona("Úrsula", "Corberó"));
		s.addActor(new Persona("Álvaro", "Morte"));
		s.addActor(new Persona("Pedro",  "Alonso"));
		s.addActor(new Persona("Itziar", "Ituño"));
		s.addActor(new Persona("Miguel", "Herrán"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Efectuar lo acordado",
				"El Profesor reúne a su banda en una casa apartada."));
		t1.addCapitulo(new Capitulo(2, "Imprudentes decisiones",
				"El asalto comienza pero hay imprevistos."));
		t1.addCapitulo(new Capitulo(3, "Erre que erre",
				"La policía rodea la fábrica."));
		t1.addCapitulo(new Capitulo(4, "Un descalabro en el plan",
				"Tokio se enfrenta a un dilema personal."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "Hagamos enfadar al enemigo",
				"La banda planea su próximo movimiento."));
		t2.addCapitulo(new Capitulo(2, "Rastreo, persecución, perdición y pasta",
				"El Profesor mantiene varios frentes abiertos."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedLosSerrano() {
		Serie s = new Serie("Los Serrano",
				"Comedia familiar sobre la convivencia de dos familias en Madrid tras "
						+ "la boda de Diego Serrano y Lucía Gómez.",
				CategoriaSerie.ESTANDAR);
		s.addCreador(new Persona("Daniel", "Écija"));
		s.addCreador(new Persona("Álex",   "Pina"));
		s.addActor(new Persona("Antonio",  "Resines"));
		s.addActor(new Persona("Belén",    "Rueda"));
		s.addActor(new Persona("Fran",     "Perea"));
		s.addActor(new Persona("Jesús",    "Bonilla"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Bienvenidos a Santa Justa",
				"Diego y Lucía deciden formar una familia conjunta."));
		t1.addCapitulo(new Capitulo(2, "Reunión de hermanos",
				"Los Serrano organizan una comida familiar."));
		t1.addCapitulo(new Capitulo(3, "El primer día de cole",
				"Curro y Teté empiezan en su nuevo colegio."));
		t1.addCapitulo(new Capitulo(4, "Vecinos en pie de guerra",
				"Aparece un nuevo vecino conflictivo en el bloque."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "Vuelta al cole",
				"Comienza un nuevo curso para los hermanos."));
		t2.addCapitulo(new Capitulo(2, "Cumpleaños sorpresa",
				"Lucía organiza una fiesta inolvidable para Diego."));
		t2.addCapitulo(new Capitulo(3, "El concierto",
				"Marcos y Eva se conocen en un concierto."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedMrRobot() {
		Serie s = new Serie("Mr. Robot",
				"Elliot, un brillante programador con trastornos sociales, es reclutado "
						+ "por un misterioso anarquista para hackear la corporación más grande del mundo.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("Sam",      "Esmail"));
		s.addActor(new Persona("Rami",       "Malek"));
		s.addActor(new Persona("Christian",  "Slater"));
		s.addActor(new Persona("Portia",     "Doubleday"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "eps1.0_hellofriend.mov",
				"Elliot conoce al misterioso Mr. Robot."));
		t1.addCapitulo(new Capitulo(2, "eps1.1_ones-and-zer0es.mpeg",
				"Elliot debe decidir si unirse a fsociety."));
		t1.addCapitulo(new Capitulo(3, "eps1.2_d3bug.mkv",
				"Elliot regresa al trabajo tras su rehabilitación."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedNarcos() {
		Serie s = new Serie("Narcos",
				"La historia real del cártel de Medellín y la persecución de Pablo Escobar "
						+ "por agentes de la DEA y autoridades colombianas.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("Chris",   "Brancato"));
		s.addCreador(new Persona("Carlo",   "Bernard"));
		s.addCreador(new Persona("Doug",    "Miro"));
		s.addActor(new Persona("Wagner",    "Moura"));
		s.addActor(new Persona("Pedro",     "Pascal"));
		s.addActor(new Persona("Boyd",      "Holbrook"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Descenso",
				"Pablo Escobar inicia su imperio del narcotráfico."));
		t1.addCapitulo(new Capitulo(2, "El bautizo",
				"La DEA establece operaciones en Colombia."));
		t1.addCapitulo(new Capitulo(3, "La gran mentira",
				"Escobar entra en política para protegerse."));
		t1.addCapitulo(new Capitulo(4, "El palacio en llamas",
				"El M-19 toma el Palacio de Justicia."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "Free at Last",
				"Escobar escapa de La Catedral."));
		t2.addCapitulo(new Capitulo(2, "Cambalache",
				"La cacería de Escobar se intensifica."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedPeakyBlinders() {
		Serie s = new Serie("Peaky Blinders",
				"En la Birmingham de la posguerra, la familia Shelby dirige una banda "
						+ "criminal con ambiciones que crecen cada temporada.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("Steven",  "Knight"));
		s.addActor(new Persona("Cillian",   "Murphy"));
		s.addActor(new Persona("Helen",     "McCrory"));
		s.addActor(new Persona("Paul",      "Anderson"));
		s.addActor(new Persona("Tom",       "Hardy"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Episode 1",
				"Tommy Shelby vuelve de la guerra y reorganiza el negocio familiar."));
		t1.addCapitulo(new Capitulo(2, "Episode 2",
				"El inspector Campbell llega a Birmingham."));
		t1.addCapitulo(new Capitulo(3, "Episode 3",
				"Los Peaky Blinders planean un golpe importante."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "Episode 1",
				"Tommy expande las operaciones a Londres."));
		t2.addCapitulo(new Capitulo(2, "Episode 2",
				"Las apuestas suben con nuevos enemigos."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedStrangerThings() {
		Serie s = new Serie("Stranger Things",
				"Un grupo de niños en un pueblo de Indiana se enfrenta a fenómenos "
						+ "sobrenaturales mientras buscan a su amigo desaparecido.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("Matt",   "Duffer"));
		s.addCreador(new Persona("Ross",   "Duffer"));
		s.addActor(new Persona("Millie",   "Bobby Brown"));
		s.addActor(new Persona("Finn",     "Wolfhard"));
		s.addActor(new Persona("David",    "Harbour"));
		s.addActor(new Persona("Winona",   "Ryder"));
		s.addActor(new Persona("Gaten",    "Matarazzo"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "The Vanishing of Will Byers",
				"Will Byers desaparece misteriosamente camino a casa."));
		t1.addCapitulo(new Capitulo(2, "The Weirdo on Maple Street",
				"Mike, Lucas y Dustin encuentran a una niña en el bosque."));
		t1.addCapitulo(new Capitulo(3, "Holly, Jolly",
				"Once revela que sabe algo sobre la desaparición de Will."));
		t1.addCapitulo(new Capitulo(4, "The Body",
				"La policía encuentra un cuerpo en el lago."));
		t1.addCapitulo(new Capitulo(5, "The Flea and the Acrobat",
				"El señor Clarke explica los universos paralelos a los niños."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "MADMAX",
				"Una nueva chica llega a Hawkins."));
		t2.addCapitulo(new Capitulo(2, "Trick or Treat, Freak",
				"Los chicos celebran Halloween mientras Will tiene visiones."));
		t2.addCapitulo(new Capitulo(3, "The Pollywog",
				"Dustin descubre una extraña criatura en su basura."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedTheWire() {
		Serie s = new Serie("The Wire",
				"Una mirada cruda y realista al tráfico de drogas en Baltimore desde "
						+ "los puntos de vista de policías, traficantes, políticos y periodistas.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("David",  "Simon"));
		s.addActor(new Persona("Dominic", "West"));
		s.addActor(new Persona("Idris",   "Elba"));
		s.addActor(new Persona("Lance",   "Reddick"));
		s.addActor(new Persona("Wendell", "Pierce"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "The Target",
				"Una unidad especial es creada para investigar a los Barksdale."));
		t1.addCapitulo(new Capitulo(2, "The Detail",
				"El equipo recibe los recursos mínimos para empezar a trabajar."));
		t1.addCapitulo(new Capitulo(3, "The Buys",
				"La unidad consigue su primera escucha telefónica."));
		s.addTemporada(t1);

		sr.save(s);
	}

	/* ============================================================== */
	/*  USUARIOS                                                       */
	/* ============================================================== */

	private void feedUsuarios() {
		Usuario john = new Usuario("john_nieve", "winterfell123",
				"ES91 2100 0418 4502 0005 1332", TipoTarifa.POR_CAPITULO);

		Usuario dany = new Usuario("daenerys", "dracarys",
				"ES12 1234 5678 9012 3456 7890", TipoTarifa.PLANA);

		Usuario tyrion = new Usuario("tyrion", "casterlyrock",
				"ES99 9999 9999 9999 9999 9999", TipoTarifa.POR_CAPITULO);

		Usuario arya = new Usuario("arya_stark", "valarmorghulis",
				"ES50 5050 5050 5050 5050 5050", TipoTarifa.POR_CAPITULO);

		ur.save(john);
		ur.save(dany);
		ur.save(tyrion);
		ur.save(arya);

		System.out.println("[AppFeeder] " + ur.count() + " usuarios cargados.");
	}

	/* ============================================================== */
	/*  ESPACIO PERSONAL Y VISUALIZACIONES                             */
	/* ============================================================== */

	private void feedVisualizaciones() {

		Usuario john   = ur.findByNombreUsuario("john_nieve").get();
		Usuario dany   = ur.findByNombreUsuario("daenerys").get();
		Usuario arya   = ur.findByNombreUsuario("arya_stark").get();

		Serie breakingBad   = sr.findByTituloIgnoreCase("Breaking Bad").get();
		Serie betterCall    = sr.findByTituloIgnoreCase("Better Call Saul").get();
		Serie chernobyl     = sr.findByTituloIgnoreCase("Chernobyl").get();
		Serie dexter        = sr.findByTituloIgnoreCase("Dexter").get();
		Serie friends       = sr.findByTituloIgnoreCase("Friends").get();
		Serie juegoTronos   = sr.findByTituloIgnoreCase("Juego de Tronos").get();
		Serie lost          = sr.findByTituloIgnoreCase("Lost").get();
		Serie casaPapel     = sr.findByTituloIgnoreCase("La Casa de Papel").get();
		Serie losSerrano    = sr.findByTituloIgnoreCase("Los Serrano").get();
		Serie mrRobot       = sr.findByTituloIgnoreCase("Mr. Robot").get();
		Serie strangerTh    = sr.findByTituloIgnoreCase("Stranger Things").get();
		Serie peakyBlinders = sr.findByTituloIgnoreCase("Peaky Blinders").get();

		// ============================================================
		// john_nieve (POR_CAPITULO) — series en los 3 estados
		// ============================================================

		// PENDIENTE
		john.agregarSerie(losSerrano);
		john.agregarSerie(strangerTh);
		john.agregarSerie(friends);
		john.agregarSerie(mrRobot);

		// EMPEZADA: Breaking Bad (medio capítulo de la T1)
		john.agregarSerie(breakingBad);
		john.visualizarCapitulo(breakingBad, 1, breakingBad.getCapitulo(1, 1).get());
		john.visualizarCapitulo(breakingBad, 1, breakingBad.getCapitulo(1, 2).get());
		john.visualizarCapitulo(breakingBad, 1, breakingBad.getCapitulo(1, 3).get());

		// EMPEZADA: Lost (varios capítulos)
		john.agregarSerie(lost);
		john.visualizarCapitulo(lost, 1, lost.getCapitulo(1, 1).get());
		john.visualizarCapitulo(lost, 1, lost.getCapitulo(1, 2).get());
		john.visualizarCapitulo(lost, 1, lost.getCapitulo(1, 3).get());
		john.visualizarCapitulo(lost, 1, lost.getCapitulo(1, 4).get());

		// TERMINADA: Chernobyl (todos los capítulos)
		john.agregarSerie(chernobyl);
		for (Temporada t : chernobyl.getTemporadas().values())
			for (Capitulo c : t.getCapitulos().values())
				john.visualizarCapitulo(chernobyl, t.getNumero(), c);

		// TERMINADA: Juego de Tronos completo (lo cargado en el feeder)
		john.agregarSerie(juegoTronos);
		for (Temporada t : juegoTronos.getTemporadas().values())
			for (Capitulo c : t.getCapitulos().values())
				john.visualizarCapitulo(juegoTronos, t.getNumero(), c);

		// ============================================================
		// daenerys (PLANA) — vea lo que vea siempre paga 20 €
		// ============================================================
		dany.agregarSerie(breakingBad);
		dany.agregarSerie(betterCall);
		dany.agregarSerie(casaPapel);
		dany.agregarSerie(peakyBlinders);
		dany.agregarSerie(juegoTronos);

		// Maratón en breaking bad
		for (Capitulo c : breakingBad.getTemporadas().get(1).getCapitulos().values())
			dany.visualizarCapitulo(breakingBad, 1, c);
		for (Capitulo c : breakingBad.getTemporadas().get(2).getCapitulos().values())
			dany.visualizarCapitulo(breakingBad, 2, c);

		// Better Call Saul
		dany.visualizarCapitulo(betterCall, 1, betterCall.getCapitulo(1, 1).get());
		dany.visualizarCapitulo(betterCall, 1, betterCall.getCapitulo(1, 2).get());
		dany.visualizarCapitulo(betterCall, 1, betterCall.getCapitulo(1, 3).get());

		// Casa de papel
		dany.visualizarCapitulo(casaPapel, 1, casaPapel.getCapitulo(1, 1).get());
		dany.visualizarCapitulo(casaPapel, 1, casaPapel.getCapitulo(1, 2).get());

		// Peaky Blinders
		dany.visualizarCapitulo(peakyBlinders, 1, peakyBlinders.getCapitulo(1, 1).get());

		// ============================================================
		// arya_stark (POR_CAPITULO) — usuaria casual
		// ============================================================
		arya.agregarSerie(dexter);
		arya.agregarSerie(strangerTh);

		// Empieza Dexter
		arya.visualizarCapitulo(dexter, 1, dexter.getCapitulo(1, 1).get());
		arya.visualizarCapitulo(dexter, 1, dexter.getCapitulo(1, 2).get());

		// Y un par de Stranger Things sin orden
		arya.visualizarCapitulo(strangerTh, 1, strangerTh.getCapitulo(1, 1).get());
		arya.visualizarCapitulo(strangerTh, 2, strangerTh.getCapitulo(2, 1).get());

		ur.save(john);
		ur.save(dany);
		ur.save(arya);

		System.out.println("[AppFeeder] Visualizaciones registradas.");
	}

	/* ============================================================== */
	/*  TEST DE REPOSITORIOS                                           */
	/* ============================================================== */

	private void testRepositorios() {

		SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-yyyy");
		Date sample = null;
		try {
			sample = dateParser.parse("01-01-2020");
		} catch (ParseException e) {
			System.out.println("Crujo parseando fecha");
			e.printStackTrace();
		}
		LocalDate sampleLocal = sample.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();

		System.out.println("================================");
		System.out.println("[Test repositorios] Fecha de muestra: " + sampleLocal);

		// ---------- SerieRepository ----------
		System.out.println("--------------------------------");
		System.out.println("[SerieRepository]");
		System.out.println("Total series en catalogo: " + sr.count());

		List<Serie> seriesPorL = sr.findByTituloStartingWithIgnoreCaseOrderByTituloAsc("L");
		System.out.println("Series que empiezan por 'L' = " + seriesPorL.size());
		for (Serie s : seriesPorL) {
			System.out.println("  " + s.getTitulo() + " (" + s.getCategoria() + ")");
		}

		List<Serie> seriesGold = sr.findByCategoriaOrderByTituloAsc(CategoriaSerie.GOLD);
		System.out.println("Series GOLD = " + seriesGold.size());
		for (Serie s : seriesGold) {
			System.out.println("  " + s.getTitulo() + " (" + s.totalCapitulos() + " caps)");
		}

		List<Serie> seriesSilver = sr.findByCategoriaOrderByTituloAsc(CategoriaSerie.SILVER);
		System.out.println("Series SILVER = " + seriesSilver.size());
		for (Serie s : seriesSilver) {
			System.out.println("  " + s.getTitulo() + " (" + s.totalCapitulos() + " caps)");
		}

		List<Serie> seriesEstandar = sr.findByCategoriaOrderByTituloAsc(CategoriaSerie.ESTANDAR);
		System.out.println("Series ESTANDAR = " + seriesEstandar.size());
		for (Serie s : seriesEstandar) {
			System.out.println("  " + s.getTitulo() + " (" + s.totalCapitulos() + " caps)");
		}

		// ---------- UsuarioRepository ----------
		System.out.println("--------------------------------");
		System.out.println("[UsuarioRepository]");

		Usuario john = ur.findByNombreUsuario("john_nieve").orElse(null);
		if (john != null) {
			System.out.println("Usuario " + john.getNombreUsuario()
					+ " (" + john.getTarifa() + ")");
			System.out.println("  Series en su espacio personal:");
			for (UsuarioSerie us : john.getSeries().values()) {
				System.out.println("    " + us.getSerie().getTitulo() + " -> " + us.getEstado());
			}
		}

		List<Usuario> usuariosPlana = ur.findByTarifa(TipoTarifa.PLANA);
		System.out.println("Usuarios con tarifa PLANA = " + usuariosPlana.size());
		for (Usuario u : usuariosPlana) {
			System.out.println("  " + u.getNombreUsuario());
		}

		List<Usuario> usuariosPorCap = ur.findByTarifa(TipoTarifa.POR_CAPITULO);
		System.out.println("Usuarios con tarifa POR_CAPITULO = " + usuariosPorCap.size());
		for (Usuario u : usuariosPorCap) {
			System.out.println("  " + u.getNombreUsuario());
		}

		// ---------- FacturaRepository ----------
		System.out.println("--------------------------------");
		System.out.println("[FacturaRepository]");

		if (john != null) {
			List<Factura> facturasJohn = fr.findByUsuarioOrderByAnioDescMesDesc(john);
			System.out.println("Facturas de " + john.getNombreUsuario()
					+ " = " + facturasJohn.size());
			for (Factura f : facturasJohn) {
				System.out.printf("  %d-%02d -> %.2f EUR  (%d lineas)%n",
						f.getAnio(), f.getMes(),
						f.calcularImporte(john.getTarifa()),
						f.getLineas().size());
				for (LineaFactura lf : f.getLineas()) {
					System.out.printf("      %s | %s T%dx%02d | %.2f EUR%n",
							lf.getFechaVisualizacion(),
							lf.getTituloSerie(),
							lf.getNumeroTemporada(),
							lf.getNumeroCapitulo(),
							lf.getCargo());
				}
			}
		}

		Usuario dany = ur.findByNombreUsuario("daenerys").orElse(null);
		if (dany != null) {
			List<Factura> facturasDany = fr.findByUsuarioOrderByAnioDescMesDesc(dany);
			System.out.println("Facturas de " + dany.getNombreUsuario()
					+ " = " + facturasDany.size());
			for (Factura f : facturasDany) {
				System.out.printf("  %d-%02d -> %.2f EUR  (tarifa PLANA, %d lineas)%n",
						f.getAnio(), f.getMes(),
						f.calcularImporte(dany.getTarifa()),
						f.getLineas().size());
			}
		}

		System.out.println("================================");
		System.out.println("Consola H2 disponible en: http://localhost:8080/h2-console");
		System.out.println("================================");
	}
}