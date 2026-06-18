package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.ProducaoComPersonagemResponseFactory.getBreakingBad;
import static com.MovieParticipations.MovieParticipations.factories.ProducaoComPersonagemResponseFactory.getMatrix;
import static com.MovieParticipations.MovieParticipations.mapper.ProducaoesAtorMapper.toResponse;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProducaoesAtorMapperTest {

    @Test
    @DisplayName("Deve transformar listas de producoes em response")
    void transformarEmResponse() {
        List<ProducaoComPersonagemResponse> filmes = of(getMatrix());
        List<ProducaoComPersonagemResponse> series = of(getBreakingBad());

        ProducaoAtorResponse response = toResponse(filmes, series);

        assertEquals(filmes, response.getFilmes());
        assertEquals(series, response.getSeries());
    }
}
