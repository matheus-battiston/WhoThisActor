package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.dto.ClassificacaoImagemResponseDTO;
import com.MovieParticipations.MovieParticipations.dto.ClassificacaoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static com.MovieParticipations.MovieParticipations.factories.response.ClassificacaoResponseDTOFactory.getKeanuReevesClassificacaoResponseDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@ExtendWith(MockitoExtension.class)
@DisplayName(RequisicaoApiClassificacaoServiceTest.NOME_CLASSE)
class RequisicaoApiClassificacaoServiceTest {

    static final String NOME_CLASSE = "RequisicaoApiClassificacaoService";
    private static final String NOME_TESTE_IMAGEM = "Deve fazer requisição multipart e retornar classificações";
    private static final String NOME_TESTE_FILTROS = "Deve fazer requisição com filtros de séries e filmes";
    private static final String NOME_TESTE_DETALHE_ERRO = "Deve fazer conversão do detalhe de erro do classificador";
    private static final String NOME_TESTE_ERRO_SEM_DETALHE = "Deve fazer uso de mensagem padrão sem detalhe";
    private static final String NOME_TESTE_RESPOSTA_SEM_CORPO = "Deve fazer rejeição de resposta sem corpo";
    private static final String NOME_TESTE_RESPOSTA_SEM_RESULTADO = "Deve fazer rejeição de resposta sem resultado";
    private static final String NOME_TESTE_SERVICO_INDISPONIVEL = "Deve fazer retorno de serviço indisponível";
    private static final String URL_CLASSIFICADOR = "http://classificador/teste";
    private static final String CAMPO_ENDERECO_CLASSIFICADOR = "CLASSIFYADDRESS";
    private static final String CAMPO_IMAGEM = "image";
    private static final String CAMPO_LISTA_SERIES = "lista_series";
    private static final String CAMPO_LISTA_FILMES = "lista_filmes";
    private static final String NOME_ARQUIVO_IMAGEM = "rosto.jpg";
    private static final String CONTEUDO_IMAGEM = "imagem";
    private static final Long ID_SERIE_FAVORITA = 1L;
    private static final Long ID_FILME_FAVORITO = 2L;
    private static final String IDS_SERIES_EM_JSON = "[1]";
    private static final String IDS_FILMES_EM_JSON = "[2]";
    private static final String DETALHE_IMAGEM_INVALIDA = "Imagem inválida";
    private static final String JSON_COM_DETALHE = "{\"detail\":\"Imagem inválida\"}";
    private static final String JSON_SEM_DETALHE = "{}";
    private static final String MENSAGEM_ERRO_DESCONHECIDO = "Erro desconhecido";
    private static final String MENSAGEM_RESPOSTA_INVALIDA = "Resposta inválida do serviço de classificação";
    private static final String MENSAGEM_SERVICO_INDISPONIVEL = "Serviço de classificação indisponível";
    private static final String MENSAGEM_ERRO_CLIENTE = "Erro";
    private static final String MENSAGEM_CONEXAO_RECUSADA = "Conexão recusada";

    @Mock
    private RestTemplate restTemplate;

    @Captor
    private ArgumentCaptor<HttpEntity<MultiValueMap<String, Object>>> requisicaoCaptor;

    private RequisicaoApiClassificacaoService service;
    private MultipartFile imagem;

    @BeforeEach
    void setUp() {
        service = new RequisicaoApiClassificacaoService(restTemplate);
        ReflectionTestUtils.setField(service, CAMPO_ENDERECO_CLASSIFICADOR, URL_CLASSIFICADOR);
        imagem = new MockMultipartFile(
                CAMPO_IMAGEM,
                NOME_ARQUIVO_IMAGEM,
                IMAGE_JPEG_VALUE,
                CONTEUDO_IMAGEM.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    @DisplayName(NOME_TESTE_IMAGEM)
    void deveFazerRequisicaoMultipartERetornarClassificacoes() throws Exception {
        ClassificacaoResponseDTO classificacao = getKeanuReevesClassificacaoResponseDTO();
        ClassificacaoImagemResponseDTO resposta = new ClassificacaoImagemResponseDTO(List.of(classificacao));

        when(restTemplate.exchange(eq(URL_CLASSIFICADOR), eq(POST), any(),
                eq(ClassificacaoImagemResponseDTO.class))).thenReturn(ResponseEntity.ok(resposta));

        List<ClassificacaoResponseDTO> classificacoes = service.classificarImagem(imagem);

        assertEquals(List.of(classificacao), classificacoes);
        verify(restTemplate).exchange(eq(URL_CLASSIFICADOR), eq(POST), requisicaoCaptor.capture(),
                eq(ClassificacaoImagemResponseDTO.class));
        HttpEntity<MultiValueMap<String, Object>> requisicao = requisicaoCaptor.getValue();
        assertEquals(MULTIPART_FORM_DATA, requisicao.getHeaders().getContentType());
        MultiValueMap<String, Object> corpo = Objects.requireNonNull(requisicao.getBody());
        Resource arquivoEnviado = (Resource) Objects.requireNonNull(corpo.getFirst(CAMPO_IMAGEM));
        assertEquals(NOME_ARQUIVO_IMAGEM, arquivoEnviado.getFilename());
    }

    @Test
    @DisplayName(NOME_TESTE_FILTROS)
    void deveFazerRequisicaoComFiltrosDeSeriesEFilmes() throws Exception {
        List<Long> idsSeries = List.of(ID_SERIE_FAVORITA);
        List<Long> idsFilmes = List.of(ID_FILME_FAVORITO);
        ClassificacaoImagemResponseDTO resposta = new ClassificacaoImagemResponseDTO(List.of());

        when(restTemplate.exchange(eq(URL_CLASSIFICADOR), eq(POST), any(),
                eq(ClassificacaoImagemResponseDTO.class))).thenReturn(ResponseEntity.ok(resposta));

        service.classificarImagem(imagem, idsSeries, idsFilmes);

        verify(restTemplate).exchange(eq(URL_CLASSIFICADOR), eq(POST), requisicaoCaptor.capture(),
                eq(ClassificacaoImagemResponseDTO.class));
        MultiValueMap<String, Object> corpo = Objects.requireNonNull(requisicaoCaptor.getValue().getBody());
        assertEquals(IDS_SERIES_EM_JSON, corpo.getFirst(CAMPO_LISTA_SERIES));
        assertEquals(IDS_FILMES_EM_JSON, corpo.getFirst(CAMPO_LISTA_FILMES));
    }

    @Test
    @DisplayName(NOME_TESTE_DETALHE_ERRO)
    void deveFazerConversaoDoDetalheDeErroDoClassificador() {
        HttpClientErrorException erroClassificador = erroDoClassificador(JSON_COM_DETALHE);

        when(restTemplate.exchange(eq(URL_CLASSIFICADOR), eq(POST), any(),
                eq(ClassificacaoImagemResponseDTO.class))).thenThrow(erroClassificador);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.classificarImagem(imagem));

        assertEquals(BAD_REQUEST, erro.getStatusCode());
        assertEquals(DETALHE_IMAGEM_INVALIDA, erro.getReason());
    }

    @Test
    @DisplayName(NOME_TESTE_ERRO_SEM_DETALHE)
    void deveFazerUsoDeMensagemPadraoSemDetalhe() {
        HttpClientErrorException erroClassificador = erroDoClassificador(JSON_SEM_DETALHE);

        when(restTemplate.exchange(eq(URL_CLASSIFICADOR), eq(POST), any(),
                eq(ClassificacaoImagemResponseDTO.class))).thenThrow(erroClassificador);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.classificarImagem(imagem));

        assertEquals(BAD_REQUEST, erro.getStatusCode());
        assertEquals(MENSAGEM_ERRO_DESCONHECIDO, erro.getReason());
    }

    @Test
    @DisplayName(NOME_TESTE_RESPOSTA_SEM_CORPO)
    void deveFazerRejeicaoDeRespostaSemCorpo() {
        ResponseEntity<ClassificacaoImagemResponseDTO> resposta = ResponseEntity.ok().build();

        when(restTemplate.exchange(eq(URL_CLASSIFICADOR), eq(POST), any(),
                eq(ClassificacaoImagemResponseDTO.class))).thenReturn(resposta);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.classificarImagem(imagem));

        assertEquals(SERVICE_UNAVAILABLE, erro.getStatusCode());
        assertEquals(MENSAGEM_RESPOSTA_INVALIDA, erro.getReason());
    }

    @Test
    @DisplayName(NOME_TESTE_RESPOSTA_SEM_RESULTADO)
    void deveFazerRejeicaoDeRespostaSemResultado() {
        ClassificacaoImagemResponseDTO corpoSemResultado = new ClassificacaoImagemResponseDTO(null);
        ResponseEntity<ClassificacaoImagemResponseDTO> resposta = ResponseEntity.ok(corpoSemResultado);

        when(restTemplate.exchange(eq(URL_CLASSIFICADOR), eq(POST), any(),
                eq(ClassificacaoImagemResponseDTO.class))).thenReturn(resposta);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.classificarImagem(imagem));

        assertEquals(SERVICE_UNAVAILABLE, erro.getStatusCode());
        assertEquals(MENSAGEM_RESPOSTA_INVALIDA, erro.getReason());
    }

    @Test
    @DisplayName(NOME_TESTE_SERVICO_INDISPONIVEL)
    void deveFazerRetornoDeServicoIndisponivel() {
        ResourceAccessException erroClassificador = new ResourceAccessException(MENSAGEM_CONEXAO_RECUSADA);

        when(restTemplate.exchange(eq(URL_CLASSIFICADOR), eq(POST), any(),
                eq(ClassificacaoImagemResponseDTO.class))).thenThrow(erroClassificador);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.classificarImagem(imagem));

        assertEquals(SERVICE_UNAVAILABLE, erro.getStatusCode());
        assertEquals(MENSAGEM_SERVICO_INDISPONIVEL, erro.getReason());
    }

    private HttpClientErrorException erroDoClassificador(String corpo) {
        return HttpClientErrorException.create(
                BAD_REQUEST,
                MENSAGEM_ERRO_CLIENTE,
                HttpHeaders.EMPTY,
                corpo.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
    }
}
