package com.rica.investigadores_service.investigadores.puertos.salida;

import java.util.Map;

public interface PublicadorEventos {

    void publicar(String topico, String clave, Map<String, Object> contenido);
    
}
