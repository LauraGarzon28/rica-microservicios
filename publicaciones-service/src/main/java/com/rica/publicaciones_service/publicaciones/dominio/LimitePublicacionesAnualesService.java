package com.rica.publicaciones_service.publicaciones.dominio;

import com.rica.publicaciones_service.publicaciones.puertos.salida.PublicacionRepositoryPort;

public class LimitePublicacionesAnualesService {

    private static final int MAXIMO_POR_ANIO = 5;

    private final PublicacionRepositoryPort publicacionRepository;

    public LimitePublicacionesAnualesService(PublicacionRepositoryPort publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    public boolean puedeRegistrar(String investigadorCorreo, int anio) {
        long registradasEsteAnio = publicacionRepository.contarPorInvestigadorYAnio(
                investigadorCorreo, anio);
        return registradasEsteAnio < MAXIMO_POR_ANIO;
    }
}