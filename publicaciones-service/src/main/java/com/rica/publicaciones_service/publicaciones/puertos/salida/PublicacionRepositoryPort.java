package com.rica.publicaciones_service.publicaciones.puertos.salida;

import com.rica.publicaciones_service.publicaciones.dominio.Publicacion;

import java.util.List;
import java.util.Optional;

public interface PublicacionRepositoryPort {

    Publicacion guardar(Publicacion publicacion);

    Optional<Publicacion> buscarPorId(String id);

    List<Publicacion> buscarPorInvestigador(String investigadorCorreo);

    long contarPorInvestigadorYAnio(String investigadorCorreo, int anio);

    void guardarTodas(List<Publicacion> publicaciones);
}
