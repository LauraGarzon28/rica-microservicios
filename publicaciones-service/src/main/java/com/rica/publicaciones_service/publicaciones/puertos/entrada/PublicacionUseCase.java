package com.rica.publicaciones_service.publicaciones.puertos.entrada;

import com.rica.publicaciones_service.publicaciones.dominio.Publicacion;

import java.util.List;

public interface PublicacionUseCase {

    Publicacion registrar(Publicacion publicacion);

    List<Publicacion> listarPorInvestigador(String investigadorCorreo);

    Publicacion buscarPorId(String id);
}
