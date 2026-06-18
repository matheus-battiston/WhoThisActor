package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.mapper.PesquisaFilmeMapper.toResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PesquisaFilmeMapperTest {

    @Test
    @DisplayName("Deve transformar entidade em response")
    void transformarEmResponse() {
        Filme filme = getMatrixComId();

        PesquisaProducaoInfoResponse response = toResponse(filme);

        assertEquals(filme.getId(), response.getId());
        assertEquals(filme.getTitulo(), response.getNome());
        assertEquals(filme.getImagem(), response.getImagem());
        assertEquals(TipoMidia.MOVIE, response.getTipoMidia());
        assertEquals(filme.getPopularidade(), response.getPopularidade());
    }
}
