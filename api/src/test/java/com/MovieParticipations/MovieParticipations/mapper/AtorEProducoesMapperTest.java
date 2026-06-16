package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.ProducaoComPersonagemResponseFactory.getBreakingBad;
import static com.MovieParticipations.MovieParticipations.factories.ProducaoComPersonagemResponseFactory.getMatrix;

@ExtendWith(MockitoExtension.class)
public class AtorEProducoesMapperTest {

    @Test
    @DisplayName("Deve transformar dados do ator e producoes em response")
    void transformarEmResponse() {
        List<ProducaoComPersonagemResponse> filmes = List.of(getMatrix());
        List<ProducaoComPersonagemResponse> series = List.of(getBreakingBad());
        ProducaoAtorResponse producoes = ProducaoAtorResponse.builder()
                .filmes(filmes)
                .series(series)
                .build();

        AtorEProducoesResponse response = AtorEProducoesMapper.toResponse(producoes, "Keanu Reeves", "/keanu.jpg", 99L, true);

        Assertions.assertEquals("Keanu Reeves", response.getNome());
        Assertions.assertEquals("/keanu.jpg", response.getUrlFoto());
        Assertions.assertEquals(99L, response.getId());
        Assertions.assertEquals(producoes, response.getProducoes());
        Assertions.assertEquals(true, response.getFavoritado());
    }
}
