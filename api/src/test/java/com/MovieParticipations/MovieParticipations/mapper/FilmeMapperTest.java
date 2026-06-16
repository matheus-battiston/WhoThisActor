package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.FilmeFactory.getMatrixComId;
import static com.MovieParticipations.MovieParticipations.factories.FilmeTMDBDtoFactory.getMatrix;
import static com.MovieParticipations.MovieParticipations.factories.ProviderDtoFactory.getNetflix;

@ExtendWith(MockitoExtension.class)
public class FilmeMapperTest {

    @Test
    @DisplayName("Deve transformar producao tmdb em entidade")
    void transformarProducaoTmdbEmEntidade() {
        ProducaoTMDBDto dto = com.MovieParticipations.MovieParticipations.factories.ProducaoTMDBDtoFactory.getMatrix();

        Filme response = FilmeMapper.toEntity(dto);

        Assertions.assertEquals(dto.getId(), response.getIdTmdb());
        Assertions.assertEquals(dto.getTitulo(), response.getTitulo());
        Assertions.assertEquals("matrix", response.getTituloNormalizado());
        Assertions.assertEquals(dto.getImagemPoster(), response.getImagem());
        Assertions.assertEquals(dto.getPopularidade(), response.getPopularidade());
        Assertions.assertEquals(false, response.getInicializado());
        Assertions.assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar filme tmdb em entidade")
    void transformarFilmeTmdbEmEntidade() {
        FilmeTMDBDto dto = getMatrix();

        Filme response = FilmeMapper.toEntity(dto);

        Assertions.assertEquals(dto.getId(), response.getIdTmdb());
        Assertions.assertEquals(dto.getTitulo(), response.getTitulo());
        Assertions.assertEquals("matrix", response.getTituloNormalizado());
        Assertions.assertEquals(dto.getImagemPoster(), response.getImagem());
        Assertions.assertEquals(dto.getPopularidade(), response.getPopularidade());
        Assertions.assertEquals(false, response.getInicializado());
        Assertions.assertNotNull(response.getUltimaAtualizacao());
    }

    @Test
    @DisplayName("Deve transformar entidade em response")
    void transformarEmResponse() {
        Filme filme = getMatrixComId();
        List<ProviderDto> providers = List.of(getNetflix());

        ProducaoInfoResponse response = FilmeMapper.toResponse(filme, providers);

        Assertions.assertEquals(filme.getId(), response.getId());
        Assertions.assertEquals(filme.getTitulo(), response.getNome());
        Assertions.assertEquals(filme.getImagem(), response.getImagem());
        Assertions.assertEquals(TipoMidia.MOVIE, response.getTipoMidia());
        Assertions.assertEquals(providers, response.getProviders());
    }
}
