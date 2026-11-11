package com.rica.consultas_service.consultas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterTopicListener {

    private static final Logger log = LoggerFactory.getLogger(DeadLetterTopicListener.class);

    @KafkaListener(topics = {
            "investigadores.registrados.DLT",
            "publicaciones.registradas.DLT",
            "investigadores.en-eliminacion.DLT",
            "investigadores.eliminados.DLT"
    }, groupId = "consultas-service-dlt-review")
    public void revisarMensajeFallido(String mensaje) {
        log.error("Mensaje enviado al Dead Letter Topic para revisión manual: {}", mensaje);
    }
}
