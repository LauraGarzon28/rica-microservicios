package com.rica.investigadores_service.investigadores.infraestructura.entrada.web;

public class InvestigadorResponse {
    
    private Long id;
    private String nombreCompleto;
    private String correoInstitucional;

    public InvestigadorResponse(Long id, String nombreCompleto, String grupoInvestigacion) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correoInstitucional = grupoInvestigacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }
}
