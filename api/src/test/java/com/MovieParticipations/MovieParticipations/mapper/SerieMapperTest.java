package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.ProviderDtoFactory.getNetflix;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;
import static com.MovieParticipations.MovieParticipations.factories.SerieTMDBDtoFactory.getBreakingBad;
import static com.MovieParticipations.MovieParticipations.mapper.SerieMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class SerieMapperTest {

    @Test
    @DisplayName("Deve transformar producao tmdb em entidade")
    void transformarProducaoTmdbEmEntidade() {
        ProducaoTMDBDto dto = com.MovieParticipations.MovieParticipations.factories.ProducaoTMDBDtoFactory.getBreakingBad();

        Serie response = toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getNome(), response.getTitulo());
        assertEquals("breaking bad", response.getTituloNormalizado());
        assertEquals(dto.getImagemPoster(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularidade());
        assertEquals(false, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar serie tmdb em entidade")
    void transformarSerieTmdbEmEntidade() {
        SerieTMDBDto dto = getBreakingBad();

        Serie response = toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getNome(), response.getTitulo());
        assertEquals("breaking bad", response.getTituloNormalizado());
        assertEquals(dto.getImagemPoster(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularidade());
        assertEquals(false, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar entidade em response")
    void transformarEmResponse() {
        Serie serie = getBreakingBadComId();
        List<ProviderDto> providers = List.of(getNetflix());

        ProducaoInfoResponse response = toResponse(serie, providers);

        assertEquals(serie.getId(), response.getId());
        assertEquals(serie.getTitulo(), response.getNome());
        assertEquals(serie.getImagem(), response.getImagem());
        assertEquals(TipoMidia.TV, response.getTipoMidia());
        assertEquals(providers, response.getProviders());
    }
}
