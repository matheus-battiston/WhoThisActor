package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;

@ExtendWith(MockitoExtension.class)
public class ProducaoMapperTest {

    @Test
    @DisplayName("Deve transformar serie em response")
    void transformarSerieEmResponse() {
        Serie serie = getBreakingBadComId();

        ProducaoResponse response = ProducaoMapper.toResponse(serie);

        Assertions.assertEquals(serie.getId(), response.getId());
        Assertions.assertEquals(serie.getTitulo(), response.getNome());
        Assertions.assertEquals(serie.getImagem(), response.getImagem());
    }

    @Test
    @DisplayName("Deve transformar filme em response")
    void transformarFilmeEmResponse() {
        Filme filme = getMatrixComId();

        ProducaoResponse response = ProducaoMapper.toResponse(filme);

        Assertions.assertEquals(filme.getId(), response.getId());
        Assertions.assertEquals(filme.getTitulo(), response.getNome());
        Assertions.assertEquals(filme.getImagem(), response.getImagem());
    }
}
