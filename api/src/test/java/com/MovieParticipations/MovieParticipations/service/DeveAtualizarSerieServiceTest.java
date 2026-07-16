package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DeveAtualizarSerieService")
class DeveAtualizarSerieServiceTest {

    private final DeveAtualizarSerieService service = new DeveAtualizarSerieService();

    @Test
    @DisplayName("Deve atualizar quando serie for nula ou info nao estiver verdadeira")
    void deveAtualizarQuandoSerieForNulaOuInfoNaoEstiverVerdadeira() {
        Serie infoNula = getBreakingBadSerieEntityComId();
        infoNula.setInfoAtualizado(null);
        Serie infoFalsa = getBreakingBadSerieEntityComId();
        infoFalsa.setInfoAtualizado(false);
        Serie infoVerdadeira = getBreakingBadSerieEntityComId();
        infoVerdadeira.setInfoAtualizado(true);

        assertThat(service.deveAtualizar(null)).isFalse();
        assertThat(service.deveAtualizar(infoNula)).isTrue();
        assertThat(service.deveAtualizar(infoFalsa)).isTrue();
        assertThat(service.deveAtualizar(infoVerdadeira)).isFalse();
    }
}
