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

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.*;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getBreakingBadProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getMatrixProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProviderDtoFactory.getNetflixProviderDto;
import static com.MovieParticipations.MovieParticipations.factories.domain.SerieFactory.getBreakingBadSerieEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieTMDBDtoFactory.getBreakingBadSerieTMDBDto;
import static com.MovieParticipations.MovieParticipations.mapper.SerieMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class SerieMapperTest {
    private static final String TITULO_NORMALIZADO_BREAKING_BAD = "breaking bad";
    private static final String TITULO_NORMALIZADO_MATRIX = "matrix";
    private static final boolean FALSO = false;

    @Test
    @DisplayName("Deve transformar producao tmdb em entidade")
    void transformarProducaoTmdbEmEntidade() {
        ProducaoTMDBDto dto =getBreakingBadProducaoTMDBDto();

        Serie response = toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getNome(), response.getTitulo());
        assertEquals(TITULO_NORMALIZADO_BREAKING_BAD, response.getTituloNormalizado());
        assertEquals(dto.getImagemPoster(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularidade());
        assertEquals(FALSO, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve usar titulo ao transformar producao de filme em entidade de serie")
    void deveUsarTituloAoTransformarProducaoDeFilmeEmEntidadeDeSerie() {
        ProducaoTMDBDto dto = getMatrixProducaoTMDBDto();

        Serie response = toEntity(dto);

        assertEquals(dto.getTitulo(), response.getTitulo());
        assertEquals(TITULO_NORMALIZADO_MATRIX, response.getTituloNormalizado());
    }

    @Test
    @DisplayName("Deve transformar serie tmdb em entidade")
    void transformarSerieTmdbEmEntidade() {
        SerieTMDBDto dto = getBreakingBadSerieTMDBDto();

        Serie response = toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getNome(), response.getTitulo());
        assertEquals(TITULO_NORMALIZADO_BREAKING_BAD, response.getTituloNormalizado());
        assertEquals(dto.getImagemPoster(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularidade());
        assertEquals(FALSO, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar entidade em response")
    void transformarEmResponse() {
        Serie serie = getBreakingBadSerieEntityComId();
        List<ProviderDto> providers = List.of(getNetflixProviderDto());

        ProducaoInfoResponse response = toResponse(serie, providers);

        assertEquals(serie.getId(), response.getId());
        assertEquals(serie.getTitulo(), response.getNome());
        assertEquals(serie.getImagem(), response.getImagem());
        assertEquals(TV, response.getTipoMidia());
        assertEquals(providers, response.getProviders());
    }
}
