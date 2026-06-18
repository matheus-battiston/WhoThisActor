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
import static com.MovieParticipations.MovieParticipations.factories.ProducaoComPersonagemResponseFactory.getBreakingBad;
import static com.MovieParticipations.MovieParticipations.factories.ProducaoComPersonagemResponseFactory.getMatrix;
import static com.MovieParticipations.MovieParticipations.mapper.AtorEProducoesMapper.*;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AtorEProducoesMapperTest {

    @Test
    @DisplayName("Deve transformar dados do ator e producoes em response")
    void transformarEmResponse() {
        List<ProducaoComPersonagemResponse> filmes = of(getMatrix());
        List<ProducaoComPersonagemResponse> series = of(getBreakingBad());
        ProducaoAtorResponse producoes = builder()
                .filmes(filmes)
                .series(series)
                .build();

        AtorEProducoesResponse response = toResponse(producoes, "Keanu Reeves", "/keanu.jpg", 99L, true);

        assertEquals("Keanu Reeves", response.getNome());
        assertEquals("/keanu.jpg", response.getUrlFoto());
        assertEquals(99L, response.getId());
        assertEquals(producoes, response.getProducoes());
        assertEquals(true, response.getFavoritado());
    }
}
