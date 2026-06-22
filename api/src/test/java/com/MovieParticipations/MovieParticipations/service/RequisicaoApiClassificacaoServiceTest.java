package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.dto.ClassificacaoImagemResponseDTO;
import com.MovieParticipations.MovieParticipations.dto.ClassificacaoResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@DisplayName("RequisicaoApiClassificacaoService")
class RequisicaoApiClassificacaoServiceTest {

    private static final String URL_CLASSIFICACAO = "http://classificador/teste";

    @Test
    @DisplayName("Deve enviar imagem em multipart e retornar classificações")
    void deveEnviarImagemEmMultipartERetornarClassificacoes() throws Exception {
        MultipartFile imagem = imagem();
        ClassificacaoResponseDTO classificacao = new ClassificacaoResponseDTO("Keanu Reeves", 0.15);
        ClassificacaoImagemResponseDTO resposta = new ClassificacaoImagemResponseDTO(List.of(classificacao));

        try (MockedConstruction<RestTemplate> restTemplates = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        eq(URL_CLASSIFICACAO), eq(POST), any(HttpEntity.class),
                        eq(ClassificacaoImagemResponseDTO.class)
                )).thenReturn(ResponseEntity.ok(resposta))
        )) {
            RequisicaoApiClassificacaoService service = criarService();

            List<ClassificacaoResponseDTO> resultado = service.classificarImagem(imagem);

            assertThat(resultado).containsExactly(classificacao);
            ArgumentCaptor<HttpEntity> requisicaoCaptor = ArgumentCaptor.forClass(HttpEntity.class);
            verify(restTemplates.constructed().get(0)).exchange(
                    eq(URL_CLASSIFICACAO), eq(POST), requisicaoCaptor.capture(), eq(ClassificacaoImagemResponseDTO.class)
            );
            assertThat(requisicaoCaptor.getValue().getHeaders().getContentType().toString())
                    .startsWith("multipart/form-data");
            assertThat(requisicaoCaptor.getValue().getBody()).isInstanceOf(org.springframework.util.MultiValueMap.class);
            org.springframework.util.MultiValueMap<String, Object> body =
                    (org.springframework.util.MultiValueMap<String, Object>) requisicaoCaptor.getValue().getBody();
            assertThat(body).containsKey("image");
        }
    }

    @Test
    @DisplayName("Deve enviar filtros de séries e filmes como JSON")
    void deveEnviarFiltrosDeSeriesEFilmesComoJson() throws Exception {
        MultipartFile imagem = imagem();
        ClassificacaoImagemResponseDTO resposta = new ClassificacaoImagemResponseDTO(List.of());

        try (MockedConstruction<RestTemplate> restTemplates = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        anyString(), eq(POST), any(HttpEntity.class), eq(ClassificacaoImagemResponseDTO.class)
                )).thenReturn(ResponseEntity.ok(resposta))
        )) {
            RequisicaoApiClassificacaoService service = criarService();

            service.classificarImagem(imagem, List.of(1L, 2L), List.of(3L));

            ArgumentCaptor<HttpEntity> requisicaoCaptor = ArgumentCaptor.forClass(HttpEntity.class);
            verify(restTemplates.constructed().get(0)).exchange(
                    eq(URL_CLASSIFICACAO), eq(POST), requisicaoCaptor.capture(), eq(ClassificacaoImagemResponseDTO.class)
            );
            org.springframework.util.MultiValueMap<String, Object> body =
                    (org.springframework.util.MultiValueMap<String, Object>) requisicaoCaptor.getValue().getBody();
            assertThat(body.getFirst("lista_series")).isEqualTo("[1,2]");
            assertThat(body.getFirst("lista_filmes")).isEqualTo("[3]");
        }
    }

    @Test
    @DisplayName("Deve converter detalhe de erro do classificador em requisição inválida")
    void deveConverterDetalheDeErroDoClassificadorEmRequisicaoInvalida() throws Exception {
        HttpClientErrorException erro = HttpClientErrorException.create(
                BAD_REQUEST,
                "Erro",
                HttpHeaders.EMPTY,
                "{\"detail\":\"Imagem inválida\"}".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        try (MockedConstruction<RestTemplate> ignored = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        anyString(), eq(POST), any(HttpEntity.class), eq(ClassificacaoImagemResponseDTO.class)
                )).thenThrow(erro)
        )) {
            RequisicaoApiClassificacaoService service = criarService();

            assertThatThrownBy(() -> service.classificarImagem(imagem()))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .satisfies(exception -> {
                        org.springframework.web.server.ResponseStatusException responseException =
                                (org.springframework.web.server.ResponseStatusException) exception;
                        assertThat(responseException.getStatusCode()).isEqualTo(BAD_REQUEST);
                        assertThat(responseException.getReason()).isEqualTo("Imagem inválida");
                    });
        }
    }

    @Test
    @DisplayName("Deve usar mensagem padrão quando erro do classificador não contiver detalhe")
    void deveUsarMensagemPadraoQuandoErroDoClassificadorNaoContiverDetalhe() throws Exception {
        HttpClientErrorException erro = HttpClientErrorException.create(
                BAD_REQUEST,
                "Erro",
                HttpHeaders.EMPTY,
                "{}".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        try (MockedConstruction<RestTemplate> ignored = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        anyString(), eq(POST), any(HttpEntity.class), eq(ClassificacaoImagemResponseDTO.class)
                )).thenThrow(erro)
        )) {
            RequisicaoApiClassificacaoService service = criarService();

            assertThatThrownBy(() -> service.classificarImagem(imagem()))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .satisfies(exception -> assertThat(
                            ((org.springframework.web.server.ResponseStatusException) exception).getReason()
                    ).isEqualTo("Erro desconhecido"));
        }
    }

    @Test
    @DisplayName("Deve usar mensagem padrão quando corpo de erro for inválido")
    void deveUsarMensagemPadraoQuandoCorpoDeErroForInvalido() throws Exception {
        HttpClientErrorException erro = HttpClientErrorException.create(
                BAD_REQUEST,
                "Erro",
                HttpHeaders.EMPTY,
                "não é JSON".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        try (MockedConstruction<RestTemplate> ignored = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        anyString(), eq(POST), any(HttpEntity.class), eq(ClassificacaoImagemResponseDTO.class)
                )).thenThrow(erro)
        )) {
            RequisicaoApiClassificacaoService service = criarService();

            assertThatThrownBy(() -> service.classificarImagem(imagem()))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .satisfies(exception -> assertThat(
                            ((org.springframework.web.server.ResponseStatusException) exception).getReason()
                    ).isEqualTo("Erro desconhecido"));
        }
    }

    @Test
    @DisplayName("Deve rejeitar resposta sem corpo ou resultado")
    void deveRejeitarRespostaSemCorpoOuResultado() throws Exception {
        try (MockedConstruction<RestTemplate> ignored = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        anyString(), eq(POST), any(HttpEntity.class), eq(ClassificacaoImagemResponseDTO.class)
                )).thenReturn(ResponseEntity.ok().build())
        )) {
            RequisicaoApiClassificacaoService service = criarService();

            assertThatThrownBy(() -> service.classificarImagem(imagem()))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .satisfies(exception -> assertThat(
                            ((org.springframework.web.server.ResponseStatusException) exception).getStatusCode()
                    ).isEqualTo(SERVICE_UNAVAILABLE));
        }
    }

    @Test
    @DisplayName("Deve rejeitar corpo de resposta sem lista de resultados")
    void deveRejeitarCorpoDeRespostaSemListaDeResultados() throws Exception {
        try (MockedConstruction<RestTemplate> ignored = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        anyString(), eq(POST), any(HttpEntity.class), eq(ClassificacaoImagemResponseDTO.class)
                )).thenReturn(ResponseEntity.ok(new ClassificacaoImagemResponseDTO(null)))
        )) {
            RequisicaoApiClassificacaoService service = criarService();

            assertThatThrownBy(() -> service.classificarImagem(imagem()))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .satisfies(exception -> assertThat(
                            ((org.springframework.web.server.ResponseStatusException) exception).getStatusCode()
                    ).isEqualTo(SERVICE_UNAVAILABLE));
        }
    }

    @Test
    @DisplayName("Deve retornar serviço indisponível após esgotar tentativas para erro de servidor")
    void deveRetornarServicoIndisponivelAposEsgotarTentativasParaErroDeServidor() throws Exception {
        HttpServerErrorException erro = HttpServerErrorException.create(
                INTERNAL_SERVER_ERROR,
                "Erro interno",
                HttpHeaders.EMPTY,
                new byte[0],
                StandardCharsets.UTF_8
        );

        try (MockedConstruction<RestTemplate> restTemplates = mockConstruction(
                RestTemplate.class,
                (restTemplate, context) -> when(restTemplate.exchange(
                        anyString(), eq(POST), any(HttpEntity.class), eq(ClassificacaoImagemResponseDTO.class)
                )).thenThrow(erro)
        )) {
            RequisicaoApiClassificacaoService service = criarService();

            assertThatThrownBy(() -> service.classificarImagem(imagem()))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .satisfies(exception -> {
                        org.springframework.web.server.ResponseStatusException responseException =
                                (org.springframework.web.server.ResponseStatusException) exception;
                        assertThat(responseException.getStatusCode()).isEqualTo(SERVICE_UNAVAILABLE);
                        assertThat(responseException.getReason())
                                .isEqualTo("Serviço de classificação indisponível após 3 tentativas");
                    });

            verify(restTemplates.constructed().get(0), times(3)).exchange(
                    eq(URL_CLASSIFICACAO), eq(POST), any(HttpEntity.class), eq(ClassificacaoImagemResponseDTO.class)
            );
        }
    }

    @Test
    @DisplayName("Deve preservar erro de cliente no callback de recuperação sem filtros")
    void devePreservarErroDeClienteNoCallbackDeRecuperacaoSemFiltros() throws Exception {
        ResponseStatusException erroOriginal = new ResponseStatusException(BAD_REQUEST, "Imagem inválida");
        RetryContext contexto = contextoComErro(erroOriginal);
        RequisicaoApiClassificacaoService service = criarServiceComRecuperacao(contexto);

        assertThatThrownBy(() -> service.classificarImagem(mock(MultipartFile.class)))
                .isSameAs(erroOriginal);
    }

    @Test
    @DisplayName("Deve retornar indisponível no callback de recuperação sem filtros")
    void deveRetornarIndisponivelNoCallbackDeRecuperacaoSemFiltros() throws Exception {
        ResourceAccessException erroOriginal = new ResourceAccessException("Conexão recusada");
        RetryContext contexto = contextoComErro(erroOriginal);
        RequisicaoApiClassificacaoService service = criarServiceComRecuperacao(contexto);

        assertThatThrownBy(() -> service.classificarImagem(mock(MultipartFile.class)))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException responseException = (ResponseStatusException) exception;
                    assertThat(responseException.getStatusCode()).isEqualTo(SERVICE_UNAVAILABLE);
                    assertThat(responseException.getCause()).isSameAs(erroOriginal);
                });
    }

    @Test
    @DisplayName("Deve preservar erro de cliente no callback de recuperação com filtros")
    void devePreservarErroDeClienteNoCallbackDeRecuperacaoComFiltros() throws Exception {
        ResponseStatusException erroOriginal = new ResponseStatusException(BAD_REQUEST, "Imagem inválida");
        RetryContext contexto = contextoComErro(erroOriginal);
        RequisicaoApiClassificacaoService service = criarServiceComRecuperacao(contexto);

        assertThatThrownBy(() -> service.classificarImagem(mock(MultipartFile.class), List.of(1L), List.of(2L)))
                .isSameAs(erroOriginal);
    }

    @Test
    @DisplayName("Deve retornar indisponível no callback de recuperação com filtros")
    void deveRetornarIndisponivelNoCallbackDeRecuperacaoComFiltros() throws Exception {
        ResourceAccessException erroOriginal = new ResourceAccessException("Conexão recusada");
        RetryContext contexto = contextoComErro(erroOriginal);
        RequisicaoApiClassificacaoService service = criarServiceComRecuperacao(contexto);

        assertThatThrownBy(() -> service.classificarImagem(mock(MultipartFile.class), List.of(1L), List.of(2L)))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    ResponseStatusException responseException = (ResponseStatusException) exception;
                    assertThat(responseException.getStatusCode()).isEqualTo(SERVICE_UNAVAILABLE);
                    assertThat(responseException.getCause()).isSameAs(erroOriginal);
                });
    }

    private RequisicaoApiClassificacaoService criarService() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(
                3,
                Map.of(ResourceAccessException.class, true, HttpServerErrorException.class, true)
        ));
        RequisicaoApiClassificacaoService service = new RequisicaoApiClassificacaoService(retryTemplate);
        ReflectionTestUtils.setField(service, "CLASSIFYADDRESS", URL_CLASSIFICACAO);
        return service;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private RequisicaoApiClassificacaoService criarServiceComRecuperacao(RetryContext contexto) {
        RetryTemplate retryTemplate = mock(RetryTemplate.class);
        when(retryTemplate.execute(any(RetryCallback.class), any(RecoveryCallback.class)))
                .thenAnswer(invocation -> ((RecoveryCallback) invocation.getArgument(1)).recover(contexto));

        RequisicaoApiClassificacaoService service = new RequisicaoApiClassificacaoService(retryTemplate);
        ReflectionTestUtils.setField(service, "CLASSIFYADDRESS", URL_CLASSIFICACAO);
        return service;
    }

    private RetryContext contextoComErro(Throwable erro) {
        RetryContext contexto = mock(RetryContext.class);
        when(contexto.getLastThrowable()).thenReturn(erro);
        return contexto;
    }

    private MultipartFile imagem() throws Exception {
        MultipartFile imagem = mock(MultipartFile.class);
        when(imagem.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{1, 2, 3}));
        when(imagem.getOriginalFilename()).thenReturn("rosto.jpg");
        return imagem;
    }
}
