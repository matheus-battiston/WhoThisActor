package com.MovieParticipations.MovieParticipations.mapper;

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
public class ProducaoesAtorMapperTest {

    @Test
    @DisplayName("Deve transformar listas de producoes em response")
    void transformarEmResponse() {
        List<ProducaoComPersonagemResponse> filmes = List.of(getMatrix());
        List<ProducaoComPersonagemResponse> series = List.of(getBreakingBad());

        ProducaoAtorResponse response = ProducaoesAtorMapper.toResponse(filmes, series);

        Assertions.assertEquals(filmes, response.getFilmes());
        Assertions.assertEquals(series, response.getSeries());
    }
}
