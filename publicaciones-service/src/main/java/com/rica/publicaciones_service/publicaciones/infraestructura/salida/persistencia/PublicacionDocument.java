package com.rica.publicaciones_service.publicaciones.infraestructura.salida.persistencia;

import com.rica.publicaciones_service.publicaciones.dominio.Publicacion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "publicaciones")
public class PublicacionDocument {

    @Id
    private String id;
    private String investigadorCorreo;
    private String titulo;
    private String tipo;
    private Integer anio;
    private Map<String, String> detalles;
    private boolean huerfana;

    public PublicacionDocument() {
    }

    public PublicacionDocument(String id, String investigadorCorreo, String titulo, String tipo,
            Integer anio, Map<String, String> detalles, boolean huerfana) {
        this.id = id;
        this.investigadorCorreo = investigadorCorreo;
        this.titulo = titulo;
        this.tipo = tipo;
        this.anio = anio;
        this.detalles = detalles;
        this.huerfana = huerfana;
    }

    public static PublicacionDocument desdeDominio(Publicacion publicacion) {
        return new PublicacionDocument(
                publicacion.getId(),
                publicacion.getInvestigadorCorreo(),
                publicacion.getTitulo(),
                publicacion.getTipo(),
                publicacion.getAnio(),
                publicacion.getDetalles(),
                publicacion.isHuerfana());
    }

    public Publicacion aDominio() {
        Publicacion publicacion = new Publicacion(
                id, investigadorCorreo, titulo, tipo, anio, detalles);
        if (huerfana) {
            publicacion.marcarComoHuerfana();
        }
        return publicacion;
    }

    public String getId() {
        return id;
    }

    public String getInvestigadorCorreo() {
        return investigadorCorreo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getAnio() {
        return anio;
    }

    public Map<String, String> getDetalles() {
        return detalles;
    }

    public boolean isHuerfana() {
        return huerfana;
    }
}
