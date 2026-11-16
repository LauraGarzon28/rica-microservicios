package com.rica.investigadores_service.dominio;

import com.rica.investigadores_service.investigadores.dominio.CorreoInstitucional;
import com.rica.investigadores_service.investigadores.dominio.EstadoInvestigador;
import com.rica.investigadores_service.investigadores.dominio.Investigador;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InvestigadorTest {

    @Test
    void creaUnInvestigadorConEstadoActivo() {
        Investigador investigador = new Investigador(
                1L,
                "Ana Torres",
                new CorreoInstitucional("ana.torres@uptc.edu.co"),
                "GIT-UPTC");

        assertThat(investigador.getId()).isEqualTo(1L);
        assertThat(investigador.getNombreCompleto()).isEqualTo("Ana Torres");
        assertThat(investigador.getCorreoInstitucional().valor())
                .isEqualTo("ana.torres@uptc.edu.co");
        assertThat(investigador.getGrupoInvestigacion()).isEqualTo("GIT-UPTC");
        assertThat(investigador.getEstado()).isEqualTo(EstadoInvestigador.ACTIVO);
        assertThat(investigador.estaEnEliminacion()).isFalse();
    }

    @Test
    void marcaElInvestigadorEnEliminacion() {
        Investigador investigador = new Investigador(
                1L,
                "Ana Torres",
                new CorreoInstitucional("ana.torres@uptc.edu.co"),
                "GIT-UPTC");

        investigador.marcarEnEliminacion();

        assertThat(investigador.getEstado()).isEqualTo(EstadoInvestigador.EN_ELIMINACION);
        assertThat(investigador.estaEnEliminacion()).isTrue();
    }

    @Test
    void rechazaUnCorreoQueNoEsInstitucional() {
        assertThatThrownBy(() -> new CorreoInstitucional("ana.torres@gmail.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("@uptc.edu.co");
    }
}
