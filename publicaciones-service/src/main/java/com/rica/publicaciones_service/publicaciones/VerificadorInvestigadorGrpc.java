package com.rica.publicaciones_service.publicaciones;

import org.springframework.stereotype.Component;

import com.rica.publicacionesservice.grpc.InvestigadorGrpcGrpc.InvestigadorGrpcBlockingStub;
import com.rica.publicacionesservice.grpc.InvestigadorRequest;

@Component 
public class VerificadorInvestigadorGrpc implements VerificadorInvestigador {
    
    private final InvestigadorGrpcBlockingStub stub;

    public VerificadorInvestigadorGrpc(InvestigadorGrpcBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public boolean existe(String correoInstitucional) {
        var request = InvestigadorRequest.newBuilder()
                .setCorreoInstitucional(correoInstitucional)
                .build();
        return stub.existeInvestigador(request).getExiste();
    }
}
