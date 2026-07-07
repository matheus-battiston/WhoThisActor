package com.MovieParticipations.MovieParticipations.controller;

import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialResponse;
import com.MovieParticipations.MovieParticipations.service.BuscaInicialService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("BuscaInicialController")
class BuscaInicialControllerTest {

    @Test
    @DisplayName("Deve retornar informações iniciais da tela de busca")
    void deveRetornarInformacoesIniciaisDaTelaDeBusca() {
        BuscaInicialResponse response = BuscaInicialResponse.builder().build();
        BuscaInicialService service = mock(BuscaInicialService.class);
        BuscaInicialController controller = new BuscaInicialController(service);
        when(service.buscar()).thenReturn(response);

        BuscaInicialResponse resultado = controller.buscar();

        assertThat(resultado).isSameAs(response);
        verify(service).buscar();
    }
}
