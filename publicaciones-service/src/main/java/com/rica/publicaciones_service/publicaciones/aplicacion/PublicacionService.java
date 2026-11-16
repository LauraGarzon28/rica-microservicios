package com.rica.publicaciones_service.publicaciones.aplicacion;

import org.springframework.stereotype.Service;

import com.rica.publicaciones_service.compartido.LimiteAnualExcedidoException;
import com.rica.publicaciones_service.compartido.RecursoNoEncontradoException;
import com.rica.publicaciones_service.publicaciones.dominio.LimitePublicacionesAnualesService;
import com.rica.publicaciones_service.publicaciones.dominio.Publicacion;
import com.rica.publicaciones_service.publicaciones.puertos.entrada.PublicacionUseCase;
import com.rica.publicaciones_service.publicaciones.puertos.salida.PublicacionRepositoryPort;
import com.rica.publicaciones_service.publicaciones.puertos.salida.PublicadorEventosPort;
import com.rica.publicaciones_service.publicaciones.puertos.salida.VerificadorInvestigadorPort;

import java.util.List;

@Service
public class PublicacionService implements PublicacionUseCase {

    private final PublicacionRepositoryPort publicacionRepository;
    private final VerificadorInvestigadorPort verificadorInvestigador;
    private final LimitePublicacionesAnualesService limitePublicacionesAnualesService;
    private final PublicadorEventosPort publicadorEventos;

    public PublicacionService(PublicacionRepositoryPort publicacionRepository,
            VerificadorInvestigadorPort verificadorInvestigador,
            LimitePublicacionesAnualesService limitePublicacionesAnualesService,
            PublicadorEventosPort publicadorEventos) {
        this.publicacionRepository = publicacionRepository;
        this.verificadorInvestigador = verificadorInvestigador;
        this.limitePublicacionesAnualesService = limitePublicacionesAnualesService;
        this.publicadorEventos = publicadorEventos;
    }

    @Override
    public Publicacion registrar(Publicacion publicacion) {
        if (!verificadorInvestigador.existe(publicacion.getInvestigadorCorreo())) {
            throw new RecursoNoEncontradoException(
                    "No existe un investigador con correo " + publicacion.getInvestigadorCorreo());
        }
        if (!limitePublicacionesAnualesService.puedeRegistrar(publicacion.getInvestigadorCorreo(), publicacion.getAnio())) {
            throw new LimiteAnualExcedidoException(
                    "El investigador " + publicacion.getInvestigadorCorreo()
                            + " ya alcanzó el máximo de publicaciones registradas para " + publicacion.getAnio());
        }
        Publicacion guardada = publicacionRepository.guardar(publicacion);
        publicadorEventos.publicacionRegistrada(guardada);
        return guardada;
    }

    @Override
    public List<Publicacion> listarPorInvestigador(String investigadorCorreo) {
        return publicacionRepository.buscarPorInvestigador(investigadorCorreo);
    }

    @Override
    public Publicacion buscarPorId(String id) {
        return publicacionRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una publicación con id " + id));
    }

}
