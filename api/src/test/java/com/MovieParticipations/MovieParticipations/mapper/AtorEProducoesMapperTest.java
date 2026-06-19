package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.controller.response.ProducaoAtorResponse.*;
import static com.MovieParticipations.MovieParticipations.factories.ProducaoComPersonagemResponseFactory.getBreakingBadProducaoComPersonagemResponse;
import static com.MovieParticipations.MovieParticipations.factories.ProducaoComPersonagemResponseFactory.getMatrixProducaoComPersonagemResponse;
import static com.MovieParticipations.MovieParticipations.mapper.AtorEProducoesMapper.*;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AtorEProducoesMapperTest {
    private static final String NOME_ATOR = "Keanu Reaves";
    private static final String IMAGEM_ATOR = "/keanu.jpg";
    private static final Long ID = 99L;
    private static final boolean ESTA_FAVORITADO = true;
    private static final boolean VERDADEIRO = true;

    @Test
    @DisplayName("Deve transformar dados do ator e producoes em response")
    void transformarEmResponse() {
        List<ProducaoComPersonagemResponse> filmes = of(getMatrixProducaoComPersonagemResponse());
        List<ProducaoComPersonagemResponse> series = of(getBreakingBadProducaoComPersonagemResponse());
        ProducaoAtorResponse producoes = builder()
                .filmes(filmes)
                .series(series)
                .build();

        AtorEProducoesResponse response = toResponse(producoes, NOME_ATOR, IMAGEM_ATOR, ID, ESTA_FAVORITADO);

        assertEquals(NOME_ATOR, response.getNome());
        assertEquals(IMAGEM_ATOR, response.getUrlFoto());
        assertEquals(ID, response.getId());
        assertEquals(producoes, response.getProducoes());
        assertEquals(VERDADEIRO, response.getFavoritado());
    }
}