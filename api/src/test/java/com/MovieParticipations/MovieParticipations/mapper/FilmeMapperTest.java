package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.factories.FilmeTMDBDtoFactory.getMatrix;
import static com.MovieParticipations.MovieParticipations.factories.ProviderDtoFactory.getNetflix;
import static com.MovieParticipations.MovieParticipations.mapper.FilmeMapper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FilmeMapperTest {

    @Test
    @DisplayName("Deve transformar producao tmdb em entidade")
    void transformarProducaoTmdbEmEntidade() {
        ProducaoTMDBDto dto = com.MovieParticipations.MovieParticipations.factories.ProducaoTMDBDtoFactory.getMatrix();

        Filme response = toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getTitulo(), response.getTitulo());
        assertEquals("matrix", response.getTituloNormalizado());
        assertEquals(dto.getImagemPoster(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularidade());
        assertEquals(false, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar filme tmdb em entidade")
    void transformarFilmeTmdbEmEntidade() {
        FilmeTMDBDto dto = getMatrix();

        Filme response = toEntity(dto);

        assertEquals(dto.getId(), response.getIdTmdb());
        assertEquals(dto.getTitulo(), response.getTitulo());
        assertEquals("matrix", response.getTituloNormalizado());
        assertEquals(dto.getImagemPoster(), response.getImagem());
        assertEquals(dto.getPopularidade(), response.getPopularidade());
        assertEquals(false, response.getInicializado());
        assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar entidade em response")
    void transformarEmResponse() {
        Filme filme = getMatrixComId();
        List<ProviderDto> providers = List.of(getNetflix());

        ProducaoInfoResponse response = toResponse(filme, providers);

        assertEquals(filme.getId(), response.getId());
        assertEquals(filme.getTitulo(), response.getNome());
        assertEquals(filme.getImagem(), response.getImagem());
        assertEquals(TipoMidia.MOVIE, response.getTipoMidia());
        assertEquals(providers, response.getProviders());
    }
}
