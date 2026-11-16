package com.rica.publicaciones_service.publicaciones.infraestructura.salida.persistencia;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PublicacionRepository extends MongoRepository<PublicacionDocument, String> {

    List<PublicacionDocument> findByInvestigadorCorreo(String investigadorCorreo);
    long countByInvestigadorCorreoAndAnio(String investigadorCorreo, Integer anio);
}
