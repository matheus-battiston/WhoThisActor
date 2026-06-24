package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.dto.BuscarProvidersDto;
import com.MovieParticipations.MovieParticipations.dto.CountryProviderDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.MOVIE;
import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.TV;
import static com.MovieParticipations.MovieParticipations.factories.tmdb.ProviderDtoFactory.getNetflixProviderDto;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.ResponseEntity.ok;

@DisplayName("ProvidersService")
class ProvidersServiceTest {

    private static final String API_KEY = "api-key-test";
    private static final Long ID_TMDB_MATRIX = 603L;
    private static final Long ID_TMDB_BREAKING_BAD = 1396L;
    private static final String URL_PROVIDERS_FILME_MATRIX =
            "https://api.themoviedb.org/3/movie/603/watch/providers?api_key=api-key-test";
    private static final String URL_PROVIDERS_SERIE_BREAKING_BAD =
            "https://api.themoviedb.org/3/tv/1396/watch/providers?api_key=api-key-test";
    private static final String BRASIL = "BR";

    @Test
    @DisplayName("Deve buscar providers de filme na URL correta")
    void deveBuscarProvidersDeFilmeNaUrlCorreta() {
        ProviderDto netflix = getNetflixProviderDto();
        BuscarProvidersDto resposta = criarRespostaComProvidersNoBrasil(of(netflix));

        RestTemplate restTemplate = mock(RestTemplate.class);
        ProvidersService service = criarService(restTemplate);
        when(restTemplate.exchange(URL_PROVIDERS_FILME_MATRIX, GET, EMPTY, BuscarProvidersDto.class))
                .thenReturn(ok(resposta));

        List<ProviderDto> resultado = service.buscarProviders(ID_TMDB_MATRIX, MOVIE);

        assertThat(resultado).containsExactly(netflix);
        verify(restTemplate).exchange(URL_PROVIDERS_FILME_MATRIX, GET, EMPTY, BuscarProvidersDto.class);
    }

    @Test
    @DisplayName("Deve buscar providers de série na URL correta")
    void deveBuscarProvidersDeSerieNaUrlCorreta() {
        ProviderDto netflix = getNetflixProviderDto();
        BuscarProvidersDto resposta = criarRespostaComProvidersNoBrasil(of(netflix));

        RestTemplate restTemplate = mock(RestTemplate.class);
        ProvidersService service = criarService(restTemplate);
        when(restTemplate.exchange(URL_PROVIDERS_SERIE_BREAKING_BAD, GET, EMPTY, BuscarProvidersDto.class))
                .thenReturn(ok(resposta));

        List<ProviderDto> resultado = service.buscarProviders(ID_TMDB_BREAKING_BAD, TV);

        assertThat(resultado).containsExactly(netflix);
        verify(restTemplate).exchange(URL_PROVIDERS_SERIE_BREAKING_BAD, GET, EMPTY, BuscarProvidersDto.class);
    }

    @Test
    @DisplayName("Deve retornar nulo quando resposta da busca for nula")
    void deveRetornarNuloQuandoRespostaDaBuscaForNula() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        ProvidersService service = criarService(restTemplate);
        when(restTemplate.exchange(URL_PROVIDERS_FILME_MATRIX, GET, EMPTY, BuscarProvidersDto.class))
                .thenReturn(ResponseEntity.ok(null));

        List<ProviderDto> resultado = service.buscarProviders(ID_TMDB_MATRIX, MOVIE);

        assertThat(resultado).isNull();
        verify(restTemplate).exchange(URL_PROVIDERS_FILME_MATRIX, GET, EMPTY, BuscarProvidersDto.class);
    }

    @Test
    @DisplayName("Deve retornar nulo quando não houver providers no Brasil")
    void deveRetornarNuloQuandoNaoHouverProvidersNoBrasil() {
        BuscarProvidersDto resposta = BuscarProvidersDto.builder().resultados(Map.of()).build();

        RestTemplate restTemplate = mock(RestTemplate.class);
        ProvidersService service = criarService(restTemplate);
        when(restTemplate.exchange(URL_PROVIDERS_FILME_MATRIX, GET, EMPTY, BuscarProvidersDto.class))
                .thenReturn(ok(resposta));

        List<ProviderDto> resultado = service.buscarProviders(ID_TMDB_MATRIX, MOVIE);

        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve retornar nulo quando providers por assinatura não estiverem disponíveis")
    void deveRetornarNuloQuandoProvidersPorAssinaturaNaoEstiveremDisponiveis() {
        CountryProviderDto providersBrasil = CountryProviderDto.builder().build();
        BuscarProvidersDto resposta = BuscarProvidersDto.builder()
                .resultados(Map.of(BRASIL, providersBrasil))
                .build();

        RestTemplate restTemplate = mock(RestTemplate.class);
        ProvidersService service = criarService(restTemplate);
        when(restTemplate.exchange(URL_PROVIDERS_FILME_MATRIX, GET, EMPTY, BuscarProvidersDto.class))
                .thenReturn(ok(resposta));

        List<ProviderDto> resultado = service.buscarProviders(ID_TMDB_MATRIX, MOVIE);

        assertThat(resultado).isNull();
    }

    private ProvidersService criarService(RestTemplate restTemplate) {
        ProvidersService service = new ProvidersService(restTemplate);
        ReflectionTestUtils.setField(service, "apiKey", API_KEY);
        return service;
    }

    private BuscarProvidersDto criarRespostaComProvidersNoBrasil(List<ProviderDto> providers) {
        CountryProviderDto providersBrasil = CountryProviderDto.builder()
                .flatrate(providers)
                .build();

        return BuscarProvidersDto.builder()
                .resultados(Map.of(BRASIL, providersBrasil))
                .build();
    }
}
