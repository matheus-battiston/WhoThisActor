package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;

@ExtendWith(MockitoExtension.class)
public class PesquisaFilmeMapperTest {

    @Test
    @DisplayName("Deve transformar entidade em response")
    void transformarEmResponse() {
        Filme filme = getMatrixComId();

        PesquisaProducaoInfoResponse response = PesquisaFilmeMapper.toResponse(filme);

        Assertions.assertEquals(filme.getId(), response.getId());
        Assertions.assertEquals(filme.getTitulo(), response.getNome());
        Assertions.assertEquals(filme.getImagem(), response.getImagem());
        Assertions.assertEquals(TipoMidia.MOVIE, response.getTipoMidia());
        Assertions.assertEquals(filme.getPopularidade(), response.getPopularidade());
    }
}
