package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.ProviderDtoFactory.getNetflix;
import static com.MovieParticipations.MovieParticipations.factories.SerieFactory.getBreakingBadComId;
import static com.MovieParticipations.MovieParticipations.factories.SerieTMDBDtoFactory.getBreakingBad;

@ExtendWith(MockitoExtension.class)
public class SerieMapperTest {

    @Test
    @DisplayName("Deve transformar producao tmdb em entidade")
    void transformarProducaoTmdbEmEntidade() {
        ProducaoTMDBDto dto = com.MovieParticipations.MovieParticipations.factories.ProducaoTMDBDtoFactory.getBreakingBad();

        Serie response = SerieMapper.toEntity(dto);

        Assertions.assertEquals(dto.getId(), response.getIdTmdb());
        Assertions.assertEquals(dto.getNome(), response.getTitulo());
        Assertions.assertEquals("breaking bad", response.getTituloNormalizado());
        Assertions.assertEquals(dto.getImagemPoster(), response.getImagem());
        Assertions.assertEquals(dto.getPopularidade(), response.getPopularidade());
        Assertions.assertEquals(false, response.getInicializado());
        Assertions.assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar serie tmdb em entidade")
    void transformarSerieTmdbEmEntidade() {
        SerieTMDBDto dto = getBreakingBad();

        Serie response = SerieMapper.toEntity(dto);

        Assertions.assertEquals(dto.getId(), response.getIdTmdb());
        Assertions.assertEquals(dto.getNome(), response.getTitulo());
        Assertions.assertEquals("breaking bad", response.getTituloNormalizado());
        Assertions.assertEquals(dto.getImagemPoster(), response.getImagem());
        Assertions.assertEquals(dto.getPopularidade(), response.getPopularidade());
        Assertions.assertEquals(false, response.getInicializado());
        Assertions.assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar entidade em response")
    void transformarEmResponse() {
        Serie serie = getBreakingBadComId();
        List<ProviderDto> providers = List.of(getNetflix());

        ProducaoInfoResponse response = SerieMapper.toResponse(serie, providers);

        Assertions.assertEquals(serie.getId(), response.getId());
        Assertions.assertEquals(serie.getTitulo(), response.getNome());
        Assertions.assertEquals(serie.getImagem(), response.getImagem());
        Assertions.assertEquals(TipoMidia.TV, response.getTipoMidia());
        Assertions.assertEquals(providers, response.getProviders());
    }
}
