package com.rica.publicaciones_service.publicaciones.infraestructura.entrada.mensajeria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.rica.publicaciones_service.publicaciones.dominio.Publicacion;
import com.rica.publicaciones_service.publicaciones.puertos.salida.PublicacionRepositoryPort;
import com.rica.publicaciones_service.publicaciones.puertos.salida.PublicadorEventosPort;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class InvestigadorEnEliminacionListener {

    private static final Logger log = LoggerFactory.getLogger(InvestigadorEnEliminacionListener.class);

    private final PublicacionRepositoryPort publicacionRepository;
    private final PublicadorEventosPort publicadorEventos;
    private final ObjectMapper objectMapper;

    public InvestigadorEnEliminacionListener(PublicacionRepositoryPort publicacionRepository,
                                             PublicadorEventosPort publicadorEventos,
                                             ObjectMapper objectMapper) {
        this.publicacionRepository = publicacionRepository;
        this.publicadorEventos = publicadorEventos;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "investigadores.en-eliminacion")
    public void alIniciarseEliminacion(String mensaje) {
        JsonNode evento = objectMapper.readTree(mensaje);
        String correo = evento.get("correoInstitucional").asString();
        String correlationId = evento.has("correlationId") ? evento.get("correlationId").asString() : "sin-correlacion";

        try (MDC.MDCCloseable ignorado = MDC.putCloseable("correlationId", correlationId)) {
            List<Publicacion> publicaciones = publicacionRepository.buscarPorInvestigador(correo);
            publicaciones.forEach(Publicacion::marcarComoHuerfana);
            publicacionRepository.guardarTodas(publicaciones);
            log.info("Saga: {} publicaciones de {} marcadas como huérfanas", publicaciones.size(), correo);

            publicadorEventos.publicacionesMarcadasHuerfanas(correo, publicaciones.size(), correlationId);
        }
    }

}