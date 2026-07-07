package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.Filme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DeveAtualizarFilmeService")
class DeveAtualizarFilmeServiceTest {

    private final DeveAtualizarFilmeService service = new DeveAtualizarFilmeService();

    @Test
    @DisplayName("Deve atualizar quando filme for nulo ou info nao estiver verdadeira")
    void deveAtualizarQuandoFilmeForNuloOuInfoNaoEstiverVerdadeira() {
        Filme infoNula = getMatrixFilmeEntityComId();
        infoNula.setInfoAtualizado(null);
        Filme infoFalsa = getMatrixFilmeEntityComId();
        infoFalsa.setInfoAtualizado(false);
        Filme infoVerdadeira = getMatrixFilmeEntityComId();
        infoVerdadeira.setInfoAtualizado(true);

        assertThat(service.deveAtualizar(null)).isFalse();
        assertThat(service.deveAtualizar(infoNula)).isTrue();
        assertThat(service.deveAtualizar(infoFalsa)).isTrue();
        assertThat(service.deveAtualizar(infoVerdadeira)).isFalse();
    }
}
