package com.rica.publicaciones_service.publicaciones.puertos.salida;

import com.rica.publicaciones_service.publicaciones.dominio.Publicacion;

public interface PublicadorEventosPort {

    void publicacionRegistrada(Publicacion publicacion);

    void publicacionesMarcadasHuerfanas(String correoInstitucional, int cantidad, String correlationId);
}
