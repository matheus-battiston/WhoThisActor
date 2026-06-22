package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.dto.ListaProducoesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.PesquisaIdPorNomeDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;

@DisplayName("RequisicaoApiService")
class RequisicaoApiServiceTest {

    private static final String API_KEY = "chave-de-teste";
    private static final String NOME_ATOR = "Keanu Reeves & Co";
    private static final Long ID_TMDB_KEANU_REEVES = 6384L;
    private static final String URL_PESQUISA_POR_NOME =
            "https://api.themoviedb.org/3/search/person?api_key=chave-de-teste&query=Keanu+Reeves+%26+Co";
    private static final String URL_CREDITOS_COMBINADOS =
            "https://api.themoviedb.org/3/person/6384/combined_credits?api_key=chave-de-teste";

    @Test
    @DisplayName("Deve chamar endpoint de pesquisa de pessoa com nome codificado")
    void deveChamarEndpointDePesquisaDePessoaComNomeCodificado() {
        PesquisaIdPorNomeDTO respostaEsperada = PesquisaIdPorNomeDTO.builder().build();

        try (MockedConstruction<RestTemplate> restTemplates = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        URL_PESQUISA_POR_NOME,
                        GET,
                        EMPTY,
                        PesquisaIdPorNomeDTO.class
                )).thenReturn(ResponseEntity.ok(respostaEsperada))
        )) {
            RequisicaoApiService service = criarService();

            PesquisaIdPorNomeDTO resultado = service.persquisarIdPorNome(NOME_ATOR);

            assertThat(resultado).isSameAs(respostaEsperada);
            assertThat(restTemplates.constructed()).hasSize(1);
            verify(restTemplates.constructed().get(0)).exchange(
                    URL_PESQUISA_POR_NOME,
                    GET,
                    EMPTY,
                    PesquisaIdPorNomeDTO.class
            );
        }
    }

    @Test
    @DisplayName("Deve chamar endpoint de créditos combinados com ID do ator")
    void deveChamarEndpointDeCreditosCombinadosComIdDoAtor() {
        ListaProducoesTMDBDto respostaEsperada = ListaProducoesTMDBDto.builder().build();

        try (MockedConstruction<RestTemplate> restTemplates = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        URL_CREDITOS_COMBINADOS,
                        GET,
                        EMPTY,
                        ListaProducoesTMDBDto.class
                )).thenReturn(ResponseEntity.ok(respostaEsperada))
        )) {
            RequisicaoApiService service = criarService();

            ListaProducoesTMDBDto resultado = service.pesquisarFilmesDoAtorPorId(ID_TMDB_KEANU_REEVES);

            assertThat(resultado).isSameAs(respostaEsperada);
            assertThat(restTemplates.constructed()).hasSize(1);
            verify(restTemplates.constructed().get(0)).exchange(
                    URL_CREDITOS_COMBINADOS,
                    GET,
                    EMPTY,
                    ListaProducoesTMDBDto.class
            );
        }
    }

    private RequisicaoApiService criarService() {
        RequisicaoApiService service = new RequisicaoApiService();
        ReflectionTestUtils.setField(service, "apiKey", API_KEY);
        return service;
    }
}
