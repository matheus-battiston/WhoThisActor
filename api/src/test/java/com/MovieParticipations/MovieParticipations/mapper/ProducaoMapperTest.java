package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.mapper.ProducaoMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProducaoMapperTest {

    @Test
    @DisplayName("Deve transformar serie em response")
    void transformarSerieEmResponse() {
        Serie serie = getBreakingBadSerieEntityComId();

        ProducaoResponse response = toResponse(serie);

        assertEquals(serie.getId(), response.getId());
        assertEquals(serie.getTitulo(), response.getNome());
        assertEquals(serie.getImagem(), response.getImagem());
    }

    @Test
    @DisplayName("Deve transformar filme em response")
    void transformarFilmeEmResponse() {
        Filme filme = getMatrixFilmeEntityComId();

        ProducaoResponse response = toResponse(filme);

        assertEquals(filme.getId(), response.getId());
        assertEquals(filme.getTitulo(), response.getNome());
        assertEquals(filme.getImagem(), response.getImagem());
    }
}
