package com.rica.publicaciones_service.publicaciones.infraestructura.salida.persistencia;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rica.publicaciones_service.publicaciones.dominio.Publicacion;
import com.rica.publicaciones_service.publicaciones.puertos.salida.PublicacionRepositoryPort;

import java.util.List;

public interface PublicacionRepository extends MongoRepository<Publicacion, String>, PublicacionRepositoryPort {

    List<Publicacion> findByInvestigadorCorreo(String investigadorCorreo);
    long countByInvestigadorCorreoAndAnio(String investigadorCorreo, Integer anio);

    @Override
    default Publicacion guardar(Publicacion publicacion) {
        return save(publicacion);
    }

    @Override
    default java.util.Optional<Publicacion> buscarPorId(String id) {
        return findById(id);
    }

    @Override
    default List<Publicacion> buscarPorInvestigador(String investigadorCorreo) {
        return findByInvestigadorCorreo(investigadorCorreo);
    }

    @Override
    default long contarPorInvestigadorYAnio(String investigadorCorreo, int anio) {
        return countByInvestigadorCorreoAndAnio(investigadorCorreo, anio);
    }

    @Override
    default void guardarTodas(List<Publicacion> publicaciones) {
        saveAll(publicaciones);
    }
}
