package es.unican.bringas.Polaflix;

import es.unican.bringas.Polaflix.dominio.*;
import es.unican.bringas.Polaflix.repositorios.FacturaRepository;
import es.unican.bringas.Polaflix.repositorios.SerieRepository;
import es.unican.bringas.Polaflix.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppFeeder implements CommandLineRunner {

	@Autowired protected UsuarioRepository ur;
	@Autowired protected SerieRepository   sr;
	@Autowired protected FacturaRepository fr;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if (sr.count() > 0 || ur.count() > 0) return;

		feedSeries();
		feedUsuarios();
		feedVisualizaciones();

		System.out.println("Application feeded");
	}

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
	}

	private void feedBreakingBad() {
		Serie s = new Serie("Breaking Bad",
				"Un profesor de química con cáncer terminal se convierte en fabricante "
						+ "de metanfetamina para asegurar el futuro económico de su familia.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("Vince", "Gilligan"));
		s.addActor(new Persona("Bryan",  "Cranston"));
		s.addActor(new Persona("Aaron",  "Paul"));
		s.addActor(new Persona("Anna",   "Gunn"));
		s.addActor(new Persona("Dean",   "Norris"));
		s.addActor(new Persona("Bob",    "Odenkirk"));

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
		s.addCreador(new Persona("Craig", "Mazin"));
		s.addActor(new Persona("Jared",   "Harris"));
		s.addActor(new Persona("Stellan", "Skarsgård"));
		s.addActor(new Persona("Emily",   "Watson"));

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
				"El juicio final y las consecuencias del desastre."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedDexter() {
		Serie s = new Serie("Dexter",
				"Un analista forense de la policía de Miami lleva una doble vida "
						+ "como asesino en serie que sólo mata a criminales.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("James",   "Manos Jr."));
		s.addActor(new Persona("Michael",  "Hall"));
		s.addActor(new Persona("Jennifer", "Carpenter"));
		s.addActor(new Persona("David",    "Zayas"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Dexter",
				"Dexter Morgan acepta un nuevo reto cuando aparece un rival inesperado."));
		t1.addCapitulo(new Capitulo(2, "Crocodile",
				"Dexter investiga a su próxima víctima mientras mantiene su fachada."));
		t1.addCapitulo(new Capitulo(3, "Popping Cherry",
				"Un flashback revela cómo Harry entrenó a Dexter."));
		t1.addCapitulo(new Capitulo(4, "Let's Give the Boy a Hand",
				"El Ice Truck Killer deja una nueva pista escalofriante."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedFriends() {
		Serie s = new Serie("Friends",
				"Las aventuras y desventuras de seis amigos viviendo en Nueva York.",
				CategoriaSerie.ESTANDAR);
		s.addCreador(new Persona("David",   "Crane"));
		s.addCreador(new Persona("Marta",   "Kauffman"));
		s.addActor(new Persona("Jennifer",  "Aniston"));
		s.addActor(new Persona("Courteney", "Cox"));
		s.addActor(new Persona("Lisa",      "Kudrow"));
		s.addActor(new Persona("Matt",      "LeBlanc"));
		s.addActor(new Persona("Matthew",   "Perry"));
		s.addActor(new Persona("David",     "Schwimmer"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "The One Where Monica Gets a Roommate",
				"Rachel deja a su prometido en el altar y llega a la vida del grupo."));
		t1.addCapitulo(new Capitulo(2, "The One with the Sonogram at the End",
				"Ross descubre que su ex esposa está embarazada."));
		t1.addCapitulo(new Capitulo(3, "The One with the Thumb",
				"Monica presenta a Alan a sus amigos y todos lo adoran."));
		t1.addCapitulo(new Capitulo(4, "The One with George Stephanopoulos",
				"Las chicas tienen una noche de pijamas mientras los chicos van al hockey."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedJuegoDeTronos() {
		Serie s = new Serie("Juego de Tronos",
				"Varias familias nobles luchan por el control del Trono de Hierro "
						+ "en el continente de Westeros.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("David", "Benioff"));
		s.addCreador(new Persona("D.B.",  "Weiss"));
		s.addActor(new Persona("Emilia",  "Clarke"));
		s.addActor(new Persona("Kit",     "Harington"));
		s.addActor(new Persona("Peter",   "Dinklage"));
		s.addActor(new Persona("Lena",    "Headey"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Winter Is Coming",
				"La familia Stark descubre que el anterior Mano del Rey ha muerto en circunstancias sospechosas."));
		t1.addCapitulo(new Capitulo(2, "The Kingsroad",
				"Ned Stark acepta ser la nueva Mano del Rey y viaja a Desembarco del Rey."));
		t1.addCapitulo(new Capitulo(3, "Lord Snow",
				"Jon Snow llega al Muro y comienza su entrenamiento."));
		t1.addCapitulo(new Capitulo(4, "Cripples, Bastards and Broken Things",
				"Tyrion visita el Muro y Ned investiga la muerte de Jon Arryn."));
		t1.addCapitulo(new Capitulo(5, "The Wolf and the Lion",
				"Un torneo es interrumpido por violencia en Desembarco del Rey."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "The North Remembers",
				"Los cinco reyes preparan sus movimientos en la guerra."));
		t2.addCapitulo(new Capitulo(2, "The Night Lands",
				"Theon Greyjoy regresa a las Islas del Hierro."));
		t2.addCapitulo(new Capitulo(3, "What Is Dead May Never Die",
				"Theon decide con cuál de sus padres se quedará."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedHouse() {
		Serie s = new Serie("House",
				"Un excéntrico y brillante médico diagnostica enfermedades raras "
						+ "con métodos poco convencionales.",
				CategoriaSerie.ESTANDAR);
		s.addCreador(new Persona("David", "Shore"));
		s.addActor(new Persona("Hugh",    "Laurie"));
		s.addActor(new Persona("Omar",    "Epps"));
		s.addActor(new Persona("Lisa",    "Edelstein"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Pilot",
				"House trata a una maestra con síntomas neurológicos inexplicables."));
		t1.addCapitulo(new Capitulo(2, "Paternity",
				"Un joven atleta con tics nerviosos desafía el diagnóstico de House."));
		t1.addCapitulo(new Capitulo(3, "Occam's Razor",
				"House cuestiona la navaja de Occam para resolver un caso complejo."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedLost() {
		Serie s = new Serie("Lost",
				"Los supervivientes de un accidente aéreo quedan varados en una misteriosa "
						+ "isla del Pacífico llena de secretos.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("J.J.",   "Abrams"));
		s.addCreador(new Persona("Damon",  "Lindelof"));
		s.addCreador(new Persona("Jeffrey","Lieber"));
		s.addActor(new Persona("Matthew",  "Fox"));
		s.addActor(new Persona("Evangeline","Lilly"));
		s.addActor(new Persona("Josh",     "Holloway"));
		s.addActor(new Persona("Terry",    "O'Quinn"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Pilot (Part 1)",
				"Los supervivientes se despiertan en la selva tras el accidente."));
		t1.addCapitulo(new Capitulo(2, "Pilot (Part 2)",
				"Un grupo explora la selva en busca del transmisor del avión."));
		t1.addCapitulo(new Capitulo(3, "Tabula Rasa",
				"Jack descubre el pasado de Kate mientras cuidan al piloto herido."));
		t1.addCapitulo(new Capitulo(4, "Walkabout",
				"Locke descubre una sorprendente capacidad que la isla le ha otorgado."));
		t1.addCapitulo(new Capitulo(5, "White Rabbit",
				"Jack persigue visiones de su padre fallecido por la jungla."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "Man of Science, Man of Faith",
				"Jack baja a la escotilla y encuentra lo que hay dentro."));
		t2.addCapitulo(new Capitulo(2, "Adrift",
				"Michael y Sawyer luchan por sobrevivir en el océano."));
		t2.addCapitulo(new Capitulo(3, "Orientation",
				"Locke y Jack aprenden el propósito de la escotilla."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedLaCasaDePapel() {
		Serie s = new Serie("La Casa de Papel",
				"Un misterioso profesor recluta a ocho ladrones para ejecutar el atraco "
						+ "perfecto a la Fábrica Nacional de Moneda y Timbre.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("Álex",  "Pina"));
		s.addActor(new Persona("Álvaro", "Morte"));
		s.addActor(new Persona("Úrsula", "Corberó"));
		s.addActor(new Persona("Itziar",  "Ituño"));
		s.addActor(new Persona("Pedro",   "Alonso"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Efectúen el cambio de vías",
				"El Profesor explica el plan maestro a su equipo de ladrones."));
		t1.addCapitulo(new Capitulo(2, "Cásate conmigo",
				"Dentro de la Fábrica, los rehenes son controlados con dificultad."));
		t1.addCapitulo(new Capitulo(3, "El precio de un sueño",
				"La inspectora Raquel Murillo lidera la negociación exterior."));
		t1.addCapitulo(new Capitulo(4, "Virtudes y contradicciones",
				"La tensión aumenta dentro y fuera de la Fábrica."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedLosSerrano() {
		Serie s = new Serie("Los Serrano",
				"Las peripecias de una familia madrileña que une a un viudo con hijos "
						+ "y una viuda con hijas.",
				CategoriaSerie.ESTANDAR);
		s.addCreador(new Persona("Pau",    "Freixas"));
		s.addActor(new Persona("Fran",     "Perea"));
		s.addActor(new Persona("Belén",    "Rueda"));
		s.addActor(new Persona("José",     "Sancho"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "El comienzo de una nueva vida",
				"Diego Serrano y Lucía se conocen y sus familias comienzan a mezclarse."));
		t1.addCapitulo(new Capitulo(2, "Un vecino muy especial",
				"La nueva familia Serrano trata de adaptarse a vivir juntos."));
		t1.addCapitulo(new Capitulo(3, "Curro cambia de bando",
				"Curro empieza a simpatizar con los Serrano pese a su reticencia inicial."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedMrRobot() {
		Serie s = new Serie("Mr. Robot",
				"Un ingeniero de ciberseguridad con problemas sociales es reclutado "
						+ "por un misterioso grupo de hackers.",
				CategoriaSerie.SILVER);
		s.addCreador(new Persona("Sam",   "Esmail"));
		s.addActor(new Persona("Rami",    "Malek"));
		s.addActor(new Persona("Christian","Slater"));
		s.addActor(new Persona("Portia",  "Doubleday"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "eps1.0_hellofriend.mov",
				"Elliot Alderson es reclutado por el misterioso Mr. Robot."));
		t1.addCapitulo(new Capitulo(2, "eps1.1_ones-and-zer0es.mpeg",
				"Elliot considera unirse a fsociety para hackear Evil Corp."));
		t1.addCapitulo(new Capitulo(3, "eps1.2_d3bug.mkv",
				"Elliot descubre que su empresa está siendo hackeada."));
		t1.addCapitulo(new Capitulo(4, "eps1.3_da3m0ns.mp4",
				"El equipo de fsociety planifica su primer gran golpe."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedNarcos() {
		Serie s = new Serie("Narcos",
				"La historia del ascenso y caída de Pablo Escobar y el Cartel de Medellín.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("Chris",  "Brancato"));
		s.addCreador(new Persona("Carlo",  "Bernard"));
		s.addCreador(new Persona("Doug",   "Miro"));
		s.addActor(new Persona("Wagner",   "Moura"));
		s.addActor(new Persona("Boyd",     "Holbrook"));
		s.addActor(new Persona("Pedro",    "Pascal"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Descenso al infierno",
				"El agente Murphy llega a Colombia y narra el ascenso de Escobar."));
		t1.addCapitulo(new Capitulo(2, "La guerra de las drogas",
				"Escobar expande su red de distribución hacia Estados Unidos."));
		t1.addCapitulo(new Capitulo(3, "El padrino de Colombia",
				"Escobar entra en política para blindarse legalmente."));
		t1.addCapitulo(new Capitulo(4, "Para matar un padrino",
				"La DEA y la policía colombiana planifican cómo atrapar a Escobar."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedPeakyBlinders() {
		Serie s = new Serie("Peaky Blinders",
				"La historia de una familia de gángsters de Birmingham que emerge como "
						+ "una fuerza criminal dominante tras la Primera Guerra Mundial.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("Steven", "Knight"));
		s.addActor(new Persona("Cillian", "Murphy"));
		s.addActor(new Persona("Tom",     "Hardy"));
		s.addActor(new Persona("Helen",   "McCrory"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Episode 1",
				"Tommy Shelby roba un cargamento de armas y desata la ira del gobierno."));
		t1.addCapitulo(new Capitulo(2, "Episode 2",
				"El inspector Chester Campbell llega a Birmingham para recuperar las armas."));
		t1.addCapitulo(new Capitulo(3, "Episode 3",
				"Tommy hace un movimiento audaz para proteger a su familia."));
		t1.addCapitulo(new Capitulo(4, "Episode 4",
				"Las tensiones entre los Shelby y la policía llegan a un punto crítico."));
		s.addTemporada(t1);

		sr.save(s);
	}

	private void feedStrangerThings() {
		Serie s = new Serie("Stranger Things",
				"Un grupo de niños en los años 80 se enfrenta a fuerzas sobrenaturales "
						+ "y experimentos gubernamentales secretos.",
				CategoriaSerie.GOLD);
		s.addCreador(new Persona("Matt",  "Duffer"));
		s.addCreador(new Persona("Ross",  "Duffer"));
		s.addActor(new Persona("Millie",  "Bobby Brown"));
		s.addActor(new Persona("Finn",    "Wolfhard"));
		s.addActor(new Persona("Winona",  "Ryder"));

		Temporada t1 = new Temporada(1);
		t1.addCapitulo(new Capitulo(1, "Chapter One: The Vanishing of Will Byers",
				"Un niño desaparece misteriosamente y sus amigos encuentran a una extraña niña."));
		t1.addCapitulo(new Capitulo(2, "Chapter Two: The Weirdo on Maple Street",
				"Los chicos esconden a Eleven mientras la policía busca a Will."));
		t1.addCapitulo(new Capitulo(3, "Chapter Three: Holly, Jolly",
				"Joyce recibe mensajes de Will a través de las luces de Navidad."));
		t1.addCapitulo(new Capitulo(4, "Chapter Four: The Body",
				"Hopper investiga la muerte de Will mientras los chicos siguen buscando."));
		s.addTemporada(t1);

		Temporada t2 = new Temporada(2);
		t2.addCapitulo(new Capitulo(1, "Chapter One: MADMAX",
				"Will tiene visiones del Upside Down mientras una nueva chica llega al pueblo."));
		t2.addCapitulo(new Capitulo(2, "Chapter Two: Trick or Treat, Freak",
				"Eleven lleva semanas escondida en la cabaña de Hopper."));
		t2.addCapitulo(new Capitulo(3, "Chapter Three: The Pollywog",
				"Will descubre una extraña criatura en el Upside Down."));
		s.addTemporada(t2);

		sr.save(s);
	}

	private void feedTheWire() {
		Serie s = new Serie("The Wire",
				"Un retrato complejo y realista del crimen organizado y la policía "
						+ "en la ciudad de Baltimore.",
				CategoriaSerie.ESTANDAR);
		s.addCreador(new Persona("David",  "Simon"));
		s.addActor(new Persona("Dominic",  "West"));
		s.addActor(new Persona("Idris",    "Elba"));
		s.addActor(new Persona("Lance",    "Reddick"));

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

	private void feedUsuarios() {
		ur.save(new Usuario("john_nieve", "winterfell123",
				"ES91 2100 0418 4502 0005 1332", TipoTarifa.POR_CAPITULO));
		ur.save(new Usuario("daenerys", "dracarys",
				"ES12 1234 5678 9012 3456 7890", TipoTarifa.PLANA));
	}

	private void feedVisualizaciones() {

		Usuario john = ur.findByNombreUsuario("john_nieve").get();
		Usuario dany = ur.findByNombreUsuario("daenerys").get();

		Serie breakingBad   = sr.findByTituloIgnoreCase("Breaking Bad").get();
		Serie betterCall    = sr.findByTituloIgnoreCase("Better Call Saul").get();
		Serie chernobyl     = sr.findByTituloIgnoreCase("Chernobyl").get();
		Serie friends       = sr.findByTituloIgnoreCase("Friends").get();
		Serie juegoTronos   = sr.findByTituloIgnoreCase("Juego de Tronos").get();
		Serie lost          = sr.findByTituloIgnoreCase("Lost").get();
		Serie casaPapel     = sr.findByTituloIgnoreCase("La Casa de Papel").get();
		Serie losSerrano    = sr.findByTituloIgnoreCase("Los Serrano").get();
		Serie mrRobot       = sr.findByTituloIgnoreCase("Mr. Robot").get();
		Serie strangerTh    = sr.findByTituloIgnoreCase("Stranger Things").get();
		Serie peakyBlinders = sr.findByTituloIgnoreCase("Peaky Blinders").get();

		john.agregarSerie(losSerrano);
		john.agregarSerie(strangerTh);
		john.agregarSerie(friends);
		john.agregarSerie(mrRobot);

		john.agregarSerie(breakingBad);
		john.visualizarCapitulo(breakingBad, 1, breakingBad.getCapitulo(1, 1).get());
		john.visualizarCapitulo(breakingBad, 1, breakingBad.getCapitulo(1, 2).get());
		john.visualizarCapitulo(breakingBad, 1, breakingBad.getCapitulo(1, 3).get());

		john.agregarSerie(lost);
		john.visualizarCapitulo(lost, 1, lost.getCapitulo(1, 1).get());
		john.visualizarCapitulo(lost, 1, lost.getCapitulo(1, 2).get());
		john.visualizarCapitulo(lost, 1, lost.getCapitulo(1, 3).get());
		john.visualizarCapitulo(lost, 1, lost.getCapitulo(1, 4).get());

		john.agregarSerie(chernobyl);
		for (Temporada t : chernobyl.getTemporadas().values())
			for (Capitulo c : t.getCapitulos().values())
				john.visualizarCapitulo(chernobyl, t.getNumero(), c);

		john.agregarSerie(juegoTronos);
		for (Temporada t : juegoTronos.getTemporadas().values())
			for (Capitulo c : t.getCapitulos().values())
				john.visualizarCapitulo(juegoTronos, t.getNumero(), c);

		dany.agregarSerie(breakingBad);
		dany.agregarSerie(betterCall);
		dany.agregarSerie(casaPapel);
		dany.agregarSerie(peakyBlinders);
		dany.agregarSerie(juegoTronos);

		for (Capitulo c : breakingBad.getTemporadas().get(1).getCapitulos().values())
			dany.visualizarCapitulo(breakingBad, 1, c);
		for (Capitulo c : breakingBad.getTemporadas().get(2).getCapitulos().values())
			dany.visualizarCapitulo(breakingBad, 2, c);

		dany.visualizarCapitulo(betterCall, 1, betterCall.getCapitulo(1, 1).get());
		dany.visualizarCapitulo(betterCall, 1, betterCall.getCapitulo(1, 2).get());
		dany.visualizarCapitulo(betterCall, 1, betterCall.getCapitulo(1, 3).get());

		dany.visualizarCapitulo(casaPapel, 1, casaPapel.getCapitulo(1, 1).get());
		dany.visualizarCapitulo(casaPapel, 1, casaPapel.getCapitulo(1, 2).get());

		dany.visualizarCapitulo(peakyBlinders, 1, peakyBlinders.getCapitulo(1, 1).get());

		ur.save(john);
		ur.save(dany);
	}
}
