package es.unican.dae.dominio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoriaSerie {

    ESTANDAR(0.5),
    SILVER(0.75),
    GOLD(1.5);

    private final double coste;
}
