package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;

@ExtendWith(MockitoExtension.class)
public class PesquisaSerieMapperTest {

    @Test
    @DisplayName("Deve transformar entidade em response")
    void transformarEmResponse() {
        Serie serie = getBreakingBadComId();

        PesquisaProducaoInfoResponse response = PesquisaSerieMapper.toResponse(serie);

        Assertions.assertEquals(serie.getId(), response.getId());
        Assertions.assertEquals(serie.getTitulo(), response.getNome());
        Assertions.assertEquals(serie.getImagem(), response.getImagem());
        Assertions.assertEquals(TipoMidia.TV, response.getTipoMidia());
        Assertions.assertEquals(serie.getPopularidade(), response.getPopularidade());
    }
}
