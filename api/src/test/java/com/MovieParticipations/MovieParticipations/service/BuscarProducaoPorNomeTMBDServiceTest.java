package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.dto.BuscarIdFilmePorNomeDTO;
import com.MovieParticipations.MovieParticipations.dto.BuscarIdSeriePorNomeDTO;
import com.MovieParticipations.MovieParticipations.dto.FilmeDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.SerieDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.factories.tmdb.BuscarIdFilmePorNomeDTOFactory.getBuscarIdFilmePorNomeDTO;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.BuscarIdSeriePorNomeDTOFactory.getBuscarIdSeriePorNomeDTO;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeDetalhesTMDBDtoFactory.getMatrixFilmeDetalhesTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeTMDBDtoFactory.getMatrixFilmeTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.FilmeTMDBDtoFactory.getMatrixRevolutionsFilmeTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieDetalhesTMDBDtoFactory.getBreakingBadSerieDetalhesTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieTMDBDtoFactory.getBreakingBadAtualizadoSerieTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieTMDBDtoFactory.getBreakingBadSerieTMDBDto;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.SerieTMDBDtoFactory.getSerieTMDBDtoSemNomeOriginal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuscarProducaoPorNomeTMBDService")
class BuscarProducaoPorNomeTMBDServiceTest {
    private static final String API_KEY = "api-key-test";
    private static final String URL_BUSCA_FILME = "https://api.themoviedb.org/3/search/movie";
    private static final String URL_BUSCA_SERIE = "https://api.themoviedb.org/3/search/tv";
    private static final String QUERY = "query";
    private static final String API_KEY_PARAM = "api_key";
    private static final String URL_DETALHE_FILME = "https://api.themoviedb.org/3/movie/123";
    private static final String URL_DETALHE_SERIE = "https://api.themoviedb.org/3/tv/456";
    private static final Long ID_TMDB_MATRIX = 123L;
    private static final Long ID_TMDB_BREAKING_BAD = 456L;
    private static final String NOME_THE_MATRIX = "the matrix";
    private static final String NOME_BREAKING_BAD = "breaking bad";
    private static final String FILME_NAO_ENCONTRADO = "Filme nao foi encontrado, verifique o nome e tente novamente";
    private static final String SERIE_NAO_ENCONTRADA = "Serie nao foi encontrada, verifique o nome e tente novamente";

    @Mock
    private RestTemplate restTemplate;

    @Captor
    private ArgumentCaptor<String> urlCaptor;

    private BuscarProducaoPorNomeTMBDService buscarProducaoPorNomeTMBDService;

    @BeforeEach
    void setUp() {
        buscarProducaoPorNomeTMBDService = new BuscarProducaoPorNomeTMBDService();
        ReflectionTestUtils.setField(buscarProducaoPorNomeTMBDService, "apiKey", API_KEY);
        ReflectionTestUtils.setField(buscarProducaoPorNomeTMBDService, "restTemplate", restTemplate);
    }

    @Test
    @DisplayName("Deve consultar endpoint de filmes com query e API key e retornar somente nome original equivalente")
    void deveConsultarEndpointDeFilmesComQueryEApiKeyERetornarSomenteNomeOriginalEquivalente() {
        FilmeTMDBDto matrix = getMatrixFilmeTMDBDto();
        FilmeTMDBDto matrixRevolutions = getMatrixRevolutionsFilmeTMDBDto();
        BuscarIdFilmePorNomeDTO response = getBuscarIdFilmePorNomeDTO(List.of(matrix, matrixRevolutions));

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdFilmePorNomeDTO.class)))
                .thenReturn(ok(response));

        List<FilmeTMDBDto> resultado = buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(NOME_THE_MATRIX);

        assertThat(resultado).containsExactly(matrix);
        verify(restTemplate).exchange(urlCaptor.capture(), eq(GET), eq(EMPTY), eq(BuscarIdFilmePorNomeDTO.class));
        assertUrlDeBusca(urlCaptor.getValue(), URL_BUSCA_FILME, NOME_THE_MATRIX);
    }

    @Test
    @DisplayName("Deve consultar endpoint de series com query e API key e retornar somente nome original equivalente")
    void deveConsultarEndpointDeSeriesComQueryEApiKeyERetornarSomenteNomeOriginalEquivalente() {
        SerieTMDBDto breakingBad = getBreakingBadSerieTMDBDto();
        SerieTMDBDto breakingBadAtualizado = getBreakingBadAtualizadoSerieTMDBDto();
        BuscarIdSeriePorNomeDTO response = getBuscarIdSeriePorNomeDTO(List.of(breakingBad, breakingBadAtualizado));

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdSeriePorNomeDTO.class)))
                .thenReturn(ok(response));

        List<SerieTMDBDto> resultado = buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(NOME_BREAKING_BAD);

        assertThat(resultado).containsExactly(breakingBad);
        verify(restTemplate).exchange(urlCaptor.capture(), eq(GET), eq(EMPTY), eq(BuscarIdSeriePorNomeDTO.class));
        assertUrlDeBusca(urlCaptor.getValue(), URL_BUSCA_SERIE, NOME_BREAKING_BAD);
    }

    @Test
    @DisplayName("Deve consultar endpoint de detalhes de filme por ID TMDB")
    void deveConsultarEndpointDeDetalhesDeFilmePorIdTmdb() {
        FilmeDetalhesTMDBDto response = getMatrixFilmeDetalhesTMDBDto();

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(FilmeDetalhesTMDBDto.class)))
                .thenReturn(ok(response));

        FilmeDetalhesTMDBDto resultado = buscarProducaoPorNomeTMBDService.buscarDetalhesFilmePorId(ID_TMDB_MATRIX);

        assertThat(resultado).isSameAs(response);
        verify(restTemplate).exchange(urlCaptor.capture(), eq(GET), eq(EMPTY), eq(FilmeDetalhesTMDBDto.class));
        assertUrlDeDetalhe(urlCaptor.getValue(), URL_DETALHE_FILME);
    }

    @Test
    @DisplayName("Deve consultar endpoint de detalhes de serie por ID TMDB")
    void deveConsultarEndpointDeDetalhesDeSeriePorIdTmdb() {
        SerieDetalhesTMDBDto response = getBreakingBadSerieDetalhesTMDBDto();

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(SerieDetalhesTMDBDto.class)))
                .thenReturn(ok(response));

        SerieDetalhesTMDBDto resultado = buscarProducaoPorNomeTMBDService.buscarDetalhesSeriePorId(ID_TMDB_BREAKING_BAD);

        assertThat(resultado).isSameAs(response);
        verify(restTemplate).exchange(urlCaptor.capture(), eq(GET), eq(EMPTY), eq(SerieDetalhesTMDBDto.class));
        assertUrlDeDetalhe(urlCaptor.getValue(), URL_DETALHE_SERIE);
    }

    @Test
    @DisplayName("Deve retornar erro de filme quando API nao retornar resposta")
    void deveRetornarErroDeFilmeQuandoApiNaoRetornarResposta() {
        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdFilmePorNomeDTO.class)))
                .thenReturn(ok(null));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(NOME_THE_MATRIX)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(FILME_NAO_ENCONTRADO);
    }

    @Test
    @DisplayName("Deve retornar erro de filme quando API retornar resultados nulos")
    void deveRetornarErroDeFilmeQuandoApiRetornarResultadosNulos() {
        BuscarIdFilmePorNomeDTO response = getBuscarIdFilmePorNomeDTO(null);

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdFilmePorNomeDTO.class)))
                .thenReturn(ok(response));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(NOME_THE_MATRIX)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(FILME_NAO_ENCONTRADO);
    }

    @Test
    @DisplayName("Deve retornar erro de filme quando API retornar resultados vazios")
    void deveRetornarErroDeFilmeQuandoApiRetornarResultadosVazios() {
        BuscarIdFilmePorNomeDTO response = getBuscarIdFilmePorNomeDTO(List.of());

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdFilmePorNomeDTO.class)))
                .thenReturn(ok(response));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(NOME_THE_MATRIX)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(FILME_NAO_ENCONTRADO);
    }

    @Test
    @DisplayName("Deve retornar erro de filme quando termo de busca for nulo")
    void deveRetornarErroDeFilmeQuandoTermoDeBuscaForNulo() {
        BuscarIdFilmePorNomeDTO response = getBuscarIdFilmePorNomeDTO(List.of(getMatrixFilmeTMDBDto()));

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdFilmePorNomeDTO.class)))
                .thenReturn(ok(response));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(null)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(FILME_NAO_ENCONTRADO);
    }

    @Test
    @DisplayName("Deve retornar erro de filme quando resultados nao tiverem nome original exato")
    void deveRetornarErroDeFilmeQuandoResultadosNaoTiveremNomeOriginalExato() {
        BuscarIdFilmePorNomeDTO response = getBuscarIdFilmePorNomeDTO(List.of(getMatrixRevolutionsFilmeTMDBDto()));

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdFilmePorNomeDTO.class)))
                .thenReturn(ok(response));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdFilmePorNome(NOME_THE_MATRIX)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(FILME_NAO_ENCONTRADO);
    }

    @Test
    @DisplayName("Deve retornar erro de serie quando API retornar resultados vazios")
    void deveRetornarErroDeSerieQuandoApiRetornarResultadosVazios() {
        BuscarIdSeriePorNomeDTO response = getBuscarIdSeriePorNomeDTO(List.of());

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdSeriePorNomeDTO.class)))
                .thenReturn(ok(response));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(NOME_BREAKING_BAD)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(SERIE_NAO_ENCONTRADA);
    }

    @Test
    @DisplayName("Deve retornar erro de serie quando API retornar resultados nulos")
    void deveRetornarErroDeSerieQuandoApiRetornarResultadosNulos() {
        BuscarIdSeriePorNomeDTO response = getBuscarIdSeriePorNomeDTO(null);

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdSeriePorNomeDTO.class)))
                .thenReturn(ok(response));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(NOME_BREAKING_BAD)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(SERIE_NAO_ENCONTRADA);
    }

    @Test
    @DisplayName("Deve retornar erro de serie quando API nao retornar resposta")
    void deveRetornarErroDeSerieQuandoApiNaoRetornarResposta() {
        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdSeriePorNomeDTO.class)))
                .thenReturn(ok(null));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(NOME_BREAKING_BAD)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(SERIE_NAO_ENCONTRADA);
    }

    @Test
    @DisplayName("Deve retornar erro de serie quando resultados nao tiverem nome original exato")
    void deveRetornarErroDeSerieQuandoResultadosNaoTiveremNomeOriginalExato() {
        BuscarIdSeriePorNomeDTO response = getBuscarIdSeriePorNomeDTO(List.of(getBreakingBadAtualizadoSerieTMDBDto()));

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdSeriePorNomeDTO.class)))
                .thenReturn(ok(response));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(NOME_BREAKING_BAD)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(SERIE_NAO_ENCONTRADA);
    }

    @Test
    @DisplayName("Deve retornar erro de serie quando nome original do resultado for nulo")
    void deveRetornarErroDeSerieQuandoNomeOriginalDoResultadoForNulo() {
        BuscarIdSeriePorNomeDTO response = getBuscarIdSeriePorNomeDTO(List.of(getSerieTMDBDtoSemNomeOriginal()));

        when(restTemplate.exchange(anyString(), eq(GET), eq(EMPTY), eq(BuscarIdSeriePorNomeDTO.class)))
                .thenReturn(ok(response));

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> buscarProducaoPorNomeTMBDService.buscarIdSeriePorNome(NOME_BREAKING_BAD)
        );

        assertThat(erro.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(erro.getReason()).isEqualTo(SERIE_NAO_ENCONTRADA);
    }

    private void assertUrlDeBusca(String url, String endpointEsperado, String nomeEsperado) {
        var uri = UriComponentsBuilder.fromUriString(url).build();

        assertThat(uri.toUriString()).startsWith(endpointEsperado);
        assertThat(uri.getQueryParams().getFirst(QUERY))
                .isEqualTo(UriUtils.encodeQueryParam(nomeEsperado, UTF_8));
        assertThat(uri.getQueryParams().getFirst(API_KEY_PARAM)).isEqualTo(API_KEY);
    }

    private void assertUrlDeDetalhe(String url, String endpointEsperado) {
        var uri = UriComponentsBuilder.fromUriString(url).build();

        assertThat(uri.toUriString()).startsWith(endpointEsperado);
        assertThat(uri.getQueryParams().getFirst(API_KEY_PARAM)).isEqualTo(API_KEY);
    }
}
