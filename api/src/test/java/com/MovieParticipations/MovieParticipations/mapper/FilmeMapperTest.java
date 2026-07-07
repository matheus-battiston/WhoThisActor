package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.dto.FilmeDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.*;
import static com.MovieParticipations.MovieParticipations.factories.domain.FilmeFactory.getMatrixFilmeEntityComId;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeDetalhesTMDBDtoFactory.getMatrixFilmeDetalhesComGenerosNulosTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeDetalhesTMDBDtoFactory.getMatrixFilmeDetalhesSemGenerosTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeDetalhesTMDBDtoFactory.getMatrixFilmeDetalhesTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeTMDBDtoFactory.getMatrixFilmeTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getMatrixProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProviderDtoFactory.getNetflixProviderDto;
import static com.MovieParticipations.MovieParticipations.mapper.FilmeMapper.*;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class FilmeMapperTest {
    private static final String TITULO_NORMALIZADO_MATRIX = "matrix";
    private static final int INDICE_PRIMEIRO_GENERO = 0;
    private static final boolean FALSO = false;

    @Test
    @DisplayName("Deve transformar producao tmdb em entidade")
    void transformarProducaoTmdbEmEntidade() {
        ProducaoTMDBDto dto = getMatrixProducaoTMDBDto();

        Filme response = toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getTitulo(), response.getTitulo());
        assertEquals(TITULO_NORMALIZADO_MATRIX, response.getTituloNormalizado());
        assertEquals(dto.getImagemPoster(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularidade());
        assertEquals(dto.getDataLancamento(), response.getDataLancamento());
        assertEquals(FALSO, response.getElencoInicializado());
        assertEquals(FALSO, response.getInfoAtualizado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar filme tmdb em entidade")
    void transformarFilmeTmdbEmEntidade() {
        FilmeTMDBDto dto = getMatrixFilmeTMDBDto();

        Filme response = toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getTitulo(), response.getTitulo());
        assertEquals(TITULO_NORMALIZADO_MATRIX, response.getTituloNormalizado());
        assertEquals(dto.getImagemPoster(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularidade());
        assertEquals(dto.getDataLancamento(), response.getDataLancamento());
        assertEquals(FALSO, response.getElencoInicializado());
        assertEquals(FALSO, response.getInfoAtualizado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar entidade em response")
    void transformarEmResponse() {
        Filme filme = getMatrixFilmeEntityComId();
        List<ProviderDto> providers = of(getNetflixProviderDto());

        ProducaoInfoResponse response = toResponse(filme, providers);

        assertEquals(filme.getId(), response.getId());
        assertEquals(filme.getTitulo(), response.getNome());
        assertEquals(filme.getImagem(), response.getImagem());
        assertEquals(filme.getBackdropPath(), response.getBackdropPath());
        assertEquals(filme.getDataLancamento(), response.getDataLancamento());
        assertEquals(filme.getGenero(), response.getGenero());
        assertEquals(filme.getOverview(), response.getOverview());
        assertEquals(MOVIE, response.getTipoMidia());
        assertEquals(providers, response.getProviders());
    }

    @Test
    @DisplayName("Deve atualizar filme com detalhes do TMDB")
    void deveAtualizarFilmeComDetalhesDoTmdb() {
        Filme filme = getMatrixFilmeEntityComId();
        filme.setInfoAtualizado(false);
        FilmeDetalhesTMDBDto detalhes = getMatrixFilmeDetalhesTMDBDto();

        atualizarComDetalhes(filme, detalhes);

        assertEquals(detalhes.getTitulo(), filme.getTitulo());
        assertEquals(TITULO_NORMALIZADO_MATRIX, filme.getTituloNormalizado());
        assertEquals(detalhes.getImagemPoster(), filme.getImagem());
        assertEquals(detalhes.getPopularidade(), filme.getPopularidade());
        assertEquals(detalhes.getDataLancamento(), filme.getDataLancamento());
        assertEquals(detalhes.getBackdropPath(), filme.getBackdropPath());
        assertEquals(detalhes.getGenres().get(INDICE_PRIMEIRO_GENERO).getName(), filme.getGenero());
        assertEquals(detalhes.getOverview(), filme.getOverview());
        assertTrue(filme.getInfoAtualizado());
        assertNotNull(filme.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve manter filme sem alterações quando detalhes forem nulos")
    void deveManterFilmeSemAlteracoesQuandoDetalhesForemNulos() {
        Filme filme = getMatrixFilmeEntityComId();
        filme.setInfoAtualizado(false);

        atualizarComDetalhes(filme, null);

        assertFalse(filme.getInfoAtualizado());
    }

    @Test
    @DisplayName("Deve atualizar genero como nulo quando detalhes nao tiverem generos")
    void deveAtualizarGeneroComoNuloQuandoDetalhesNaoTiveremGeneros() {
        Filme filmeSemGeneros = getMatrixFilmeEntityComId();
        Filme filmeComGenerosNulos = getMatrixFilmeEntityComId();

        atualizarComDetalhes(filmeSemGeneros, getMatrixFilmeDetalhesSemGenerosTMDBDto());
        atualizarComDetalhes(filmeComGenerosNulos, getMatrixFilmeDetalhesComGenerosNulosTMDBDto());

        assertNull(filmeSemGeneros.getGenero());
        assertNull(filmeComGenerosNulos.getGenero());
    }
}
