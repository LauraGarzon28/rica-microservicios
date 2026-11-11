package com.rica.publicaciones_service.publicaciones.infraestructura.configuracion;

import com.rica.publicaciones_service.publicaciones.dominio.LimitePublicacionesAnualesService;
import com.rica.publicaciones_service.publicaciones.puertos.salida.PublicacionRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublicacionesBeanConfiguration {

    @Bean
    public LimitePublicacionesAnualesService limitePublicacionesAnualesService(
            PublicacionRepositoryPort publicacionRepository) {
        return new LimitePublicacionesAnualesService(publicacionRepository);
    }
}
