package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
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
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeTMDBDtoFactory.getMatrixFilmeTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProducaoTMDBDtoFactory.getMatrixProducaoTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProviderDtoFactory.getNetflixProviderDto;
import static com.MovieParticipations.MovieParticipations.mapper.FilmeMapper.*;
import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FilmeMapperTest {
    private static final String TITULO_NORMALIZADO_MATRIX = "matrix";
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
        assertEquals(FALSO, response.getInicializado());
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
        assertEquals(FALSO, response.getInicializado());
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
        assertEquals(MOVIE, response.getTipoMidia());
        assertEquals(providers, response.getProviders());
    }
}
