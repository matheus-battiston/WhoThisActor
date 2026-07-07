package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.dto.SerieDetalhesTMDBDto;
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
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieDetalhesTMDBDtoFactory.getBreakingBadSerieDetalhesComDatasInvalidasTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieDetalhesTMDBDtoFactory.getBreakingBadSerieDetalhesComAnoNaoNumericoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieDetalhesTMDBDtoFactory.getBreakingBadSerieDetalhesComDatasNulasTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieDetalhesTMDBDtoFactory.getBreakingBadSerieDetalhesComGenerosNulosTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieDetalhesTMDBDtoFactory.getBreakingBadSerieDetalhesSemGenerosTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieDetalhesTMDBDtoFactory.getBreakingBadSerieDetalhesTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieTMDBDtoFactory.getBreakingBadSerieTMDBDto;
import static com.MovieParticipations.MovieParticipations.mapper.SerieMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SerieMapperTest {
    private static final String TITULO_NORMALIZADO_BREAKING_BAD = "breaking bad";
    private static final String TITULO_NORMALIZADO_MATRIX = "matrix";
    private static final Integer ANO_PRIMEIRA_TEMPORADA_BREAKING_BAD = 2008;
    private static final Integer ANO_ULTIMA_TEMPORADA_BREAKING_BAD = 2013;
    private static final int INDICE_PRIMEIRO_GENERO = 0;
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
        assertEquals(FALSO, response.getElencoInicializado());
        assertEquals(FALSO, response.getInfoAtualizado());
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
        assertEquals(FALSO, response.getElencoInicializado());
        assertEquals(FALSO, response.getInfoAtualizado());
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
        assertEquals(serie.getBackdropPath(), response.getBackdropPath());
        assertEquals(serie.getAnoPrimeiraTemporada(), response.getAnoPrimeiraTemporada());
        assertEquals(serie.getAnoUltimaTemporada(), response.getAnoUltimaTemporada());
        assertEquals(serie.getQuantidadeTemporadas(), response.getQuantidadeTemporadas());
        assertEquals(serie.getGenero(), response.getGenero());
        assertEquals(serie.getOverview(), response.getOverview());
        assertEquals(TV, response.getTipoMidia());
        assertEquals(providers, response.getProviders());
    }

    @Test
    @DisplayName("Deve atualizar serie com detalhes do TMDB")
    void deveAtualizarSerieComDetalhesDoTmdb() {
        Serie serie = getBreakingBadSerieEntityComId();
        serie.setInfoAtualizado(false);
        SerieDetalhesTMDBDto detalhes = getBreakingBadSerieDetalhesTMDBDto();

        atualizarComDetalhes(serie, detalhes);

        assertEquals(detalhes.getNome(), serie.getTitulo());
        assertEquals(TITULO_NORMALIZADO_BREAKING_BAD, serie.getTituloNormalizado());
        assertEquals(detalhes.getImagemPoster(), serie.getImagem());
        assertEquals(detalhes.getPopularidade(), serie.getPopularidade());
        assertEquals(detalhes.getBackdropPath(), serie.getBackdropPath());
        assertEquals(ANO_PRIMEIRA_TEMPORADA_BREAKING_BAD, serie.getAnoPrimeiraTemporada());
        assertEquals(ANO_ULTIMA_TEMPORADA_BREAKING_BAD, serie.getAnoUltimaTemporada());
        assertEquals(detalhes.getQuantidadeTemporadas(), serie.getQuantidadeTemporadas());
        assertEquals(detalhes.getGenres().get(INDICE_PRIMEIRO_GENERO).getName(), serie.getGenero());
        assertEquals(detalhes.getOverview(), serie.getOverview());
        assertTrue(serie.getInfoAtualizado());
        assertNotNull(serie.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve manter serie sem alterações quando detalhes forem nulos")
    void deveManterSerieSemAlteracoesQuandoDetalhesForemNulos() {
        Serie serie = getBreakingBadSerieEntityComId();
        serie.setInfoAtualizado(false);

        atualizarComDetalhes(serie, null);

        assertFalse(serie.getInfoAtualizado());
    }

    @Test
    @DisplayName("Deve atualizar genero como nulo quando detalhes nao tiverem generos")
    void deveAtualizarGeneroComoNuloQuandoDetalhesNaoTiveremGeneros() {
        Serie serieSemGeneros = getBreakingBadSerieEntityComId();
        Serie serieComGenerosNulos = getBreakingBadSerieEntityComId();

        atualizarComDetalhes(serieSemGeneros, getBreakingBadSerieDetalhesSemGenerosTMDBDto());
        atualizarComDetalhes(serieComGenerosNulos, getBreakingBadSerieDetalhesComGenerosNulosTMDBDto());

        assertNull(serieSemGeneros.getGenero());
        assertNull(serieComGenerosNulos.getGenero());
    }

    @Test
    @DisplayName("Deve deixar anos nulos quando datas forem invalidas")
    void deveDeixarAnosNulosQuandoDatasForemInvalidas() {
        Serie serie = getBreakingBadSerieEntityComId();

        atualizarComDetalhes(serie, getBreakingBadSerieDetalhesComDatasInvalidasTMDBDto());

        assertNull(serie.getAnoPrimeiraTemporada());
        assertNull(serie.getAnoUltimaTemporada());
    }

    @Test
    @DisplayName("Deve deixar anos nulos quando datas forem nulas")
    void deveDeixarAnosNulosQuandoDatasForemNulas() {
        Serie serie = getBreakingBadSerieEntityComId();

        atualizarComDetalhes(serie, getBreakingBadSerieDetalhesComDatasNulasTMDBDto());

        assertNull(serie.getAnoPrimeiraTemporada());
        assertNull(serie.getAnoUltimaTemporada());
    }

    @Test
    @DisplayName("Deve deixar anos nulos quando ano nao for numerico")
    void deveDeixarAnosNulosQuandoAnoNaoForNumerico() {
        Serie serie = getBreakingBadSerieEntityComId();

        atualizarComDetalhes(serie, getBreakingBadSerieDetalhesComAnoNaoNumericoTMDBDto());

        assertNull(serie.getAnoPrimeiraTemporada());
        assertNull(serie.getAnoUltimaTemporada());
    }
}
