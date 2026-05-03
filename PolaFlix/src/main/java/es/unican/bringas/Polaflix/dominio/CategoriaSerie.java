package es.unican.bringas.Polaflix.dominio;

public enum CategoriaSerie {

    ESTANDAR(0.50), SILVER(0.75), GOLD(1.50);

    private final double costePorCapitulo;

    CategoriaSerie(double costePorCapitulo) {
        this.costePorCapitulo = costePorCapitulo;
    }

    public double getCoste() { return costePorCapitulo; }
}
