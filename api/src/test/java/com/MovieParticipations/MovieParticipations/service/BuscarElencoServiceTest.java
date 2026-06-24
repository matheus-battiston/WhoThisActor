package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDtoMovie;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDtoSerie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBMovieDtoFactory.getKeanuReevesAtorTMDBMovieDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBMovieDtoFactory.getKeanuReevesDiretorAtorTMDBMovieDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBSerieDtoFactory.getAaronPaulRoteiristaAtorTMDBSerieDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.AtorTMDBSerieDtoFactory.getBryanCranstonAtorTMDBSerieDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.CastResponseDtoMovieFactory.getCastResponseDtoMovie;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.CastResponseDtoSerieFactory.getCastResponseDtoSerie;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.ResponseEntity.ok;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuscarElencoService")
class BuscarElencoServiceTest {
    private static final String API_KEY = "api-key-test";
    private static final Long ID_TMDB_MATRIX = 123L;
    private static final Long ID_TMDB_BREAKING_BAD = 456L;
    private static final String URL_ELENCO_FILME_MATRIX =
            "https://api.themoviedb.org/3/movie/123/credits?api_key=api-key-test";
    private static final String URL_ELENCO_SERIE_BREAKING_BAD =
            "https://api.themoviedb.org/3/tv/456/aggregate_credits?api_key=api-key-test";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BuscarElencoService buscarElencoService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(buscarElencoService, "apiKey", API_KEY);
    }

    @Test
    @DisplayName("Deve buscar elenco de filme na URL correta e retornar apenas atores")
    void deveBuscarElencoDeFilmeNaUrlCorretaERetornarApenasAtores() {
        AtorTMDBMovieDto ator = getKeanuReevesAtorTMDBMovieDto();
        AtorTMDBMovieDto diretor = getKeanuReevesDiretorAtorTMDBMovieDto();
        CastResponseDtoMovie response = getCastResponseDtoMovie(of(ator, diretor));

        when(restTemplate.exchange(URL_ELENCO_FILME_MATRIX, GET, EMPTY, CastResponseDtoMovie.class))
                .thenReturn(ok(response));

        List<AtorTMDBMovieDto> resultado = buscarElencoService.pesquisaElencoFilme(ID_TMDB_MATRIX);

        assertThat(resultado).containsExactly(ator);
        verify(restTemplate).exchange(URL_ELENCO_FILME_MATRIX, GET, EMPTY, CastResponseDtoMovie.class);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando resposta de filme for nula")
    void deveRetornarListaVaziaQuandoRespostaDeFilmeForNula() {
        when(restTemplate.exchange(URL_ELENCO_FILME_MATRIX, GET, EMPTY, CastResponseDtoMovie.class))
                .thenReturn(ok(null));

        List<AtorTMDBMovieDto> resultado = buscarElencoService.pesquisaElencoFilme(ID_TMDB_MATRIX);

        assertThat(resultado).isEmpty();
        verify(restTemplate).exchange(URL_ELENCO_FILME_MATRIX, GET, EMPTY, CastResponseDtoMovie.class);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando elenco de filme for nulo")
    void deveRetornarListaVaziaQuandoElencoDeFilmeForNulo() {
        CastResponseDtoMovie response = getCastResponseDtoMovie(null);

        when(restTemplate.exchange(URL_ELENCO_FILME_MATRIX, GET, EMPTY, CastResponseDtoMovie.class))
                .thenReturn(ok(response));

        List<AtorTMDBMovieDto> resultado = buscarElencoService.pesquisaElencoFilme(ID_TMDB_MATRIX);

        assertThat(resultado).isEmpty();
        verify(restTemplate).exchange(URL_ELENCO_FILME_MATRIX, GET, EMPTY, CastResponseDtoMovie.class);
    }

    @Test
    @DisplayName("Deve buscar elenco de serie na URL correta e retornar apenas atores")
    void deveBuscarElencoDeSerieNaUrlCorretaERetornarApenasAtores() {
        AtorTMDBSerieDto ator = getBryanCranstonAtorTMDBSerieDto();
        AtorTMDBSerieDto roteirista = getAaronPaulRoteiristaAtorTMDBSerieDto();
        CastResponseDtoSerie response = getCastResponseDtoSerie(of(ator, roteirista));

        when(restTemplate.exchange(URL_ELENCO_SERIE_BREAKING_BAD, GET, EMPTY, CastResponseDtoSerie.class))
                .thenReturn(ok(response));

        List<AtorTMDBSerieDto> resultado = buscarElencoService.pesquisaElencoSerie(ID_TMDB_BREAKING_BAD);

        assertThat(resultado).containsExactly(ator);
        verify(restTemplate).exchange(URL_ELENCO_SERIE_BREAKING_BAD, GET, EMPTY, CastResponseDtoSerie.class);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando resposta de serie for nula")
    void deveRetornarListaVaziaQuandoRespostaDeSerieForNula() {
        when(restTemplate.exchange(URL_ELENCO_SERIE_BREAKING_BAD, GET, EMPTY, CastResponseDtoSerie.class))
                .thenReturn(ok(null));

        List<AtorTMDBSerieDto> resultado = buscarElencoService.pesquisaElencoSerie(ID_TMDB_BREAKING_BAD);

        assertThat(resultado).isEmpty();
        verify(restTemplate).exchange(URL_ELENCO_SERIE_BREAKING_BAD, GET, EMPTY, CastResponseDtoSerie.class);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando elenco de serie for nulo")
    void deveRetornarListaVaziaQuandoElencoDeSerieForNulo() {
        CastResponseDtoSerie response = getCastResponseDtoSerie(null);

        when(restTemplate.exchange(URL_ELENCO_SERIE_BREAKING_BAD, GET, EMPTY, CastResponseDtoSerie.class))
                .thenReturn(ok(response));

        List<AtorTMDBSerieDto> resultado = buscarElencoService.pesquisaElencoSerie(ID_TMDB_BREAKING_BAD);

        assertThat(resultado).isEmpty();
        verify(restTemplate).exchange(URL_ELENCO_SERIE_BREAKING_BAD, GET, EMPTY, CastResponseDtoSerie.class);
    }
}
