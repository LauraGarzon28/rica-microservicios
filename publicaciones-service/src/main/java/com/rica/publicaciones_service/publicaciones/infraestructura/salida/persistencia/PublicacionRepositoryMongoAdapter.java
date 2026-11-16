package com.rica.publicaciones_service.publicaciones.infraestructura.salida.persistencia;

import com.rica.publicaciones_service.publicaciones.dominio.Publicacion;
import com.rica.publicaciones_service.publicaciones.puertos.salida.PublicacionRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PublicacionRepositoryMongoAdapter implements PublicacionRepositoryPort {

    private final PublicacionRepository repository;

    public PublicacionRepositoryMongoAdapter(PublicacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Publicacion guardar(Publicacion publicacion) {
        return repository.save(PublicacionDocument.desdeDominio(publicacion)).aDominio();
    }

    @Override
    public Optional<Publicacion> buscarPorId(String id) {
        return repository.findById(id).map(PublicacionDocument::aDominio);
    }

    @Override
    public List<Publicacion> buscarPorInvestigador(String investigadorCorreo) {
        return repository.findByInvestigadorCorreo(investigadorCorreo)
                .stream()
                .map(PublicacionDocument::aDominio)
                .toList();
    }

    @Override
    public long contarPorInvestigadorYAnio(String investigadorCorreo, int anio) {
        return repository.countByInvestigadorCorreoAndAnio(investigadorCorreo, anio);
    }

    @Override
    public void guardarTodas(List<Publicacion> publicaciones) {
        repository.saveAll(publicaciones.stream()
                .map(PublicacionDocument::desdeDominio)
                .toList());
    }
}
