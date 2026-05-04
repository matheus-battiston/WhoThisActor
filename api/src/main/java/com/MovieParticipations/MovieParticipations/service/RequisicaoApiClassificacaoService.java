package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.dto.ClassificacaoImagemResponseDTO;
import com.MovieParticipations.MovieParticipations.dto.ClassificacaoResponseDTO;
import com.MovieParticipations.MovieParticipations.util.MultipartInputStreamFileResource;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Service
public class RequisicaoApiClassificacaoService {

    private static final String IMAGEM = "image";
    private static final String DETALHE = "detail";
    private static final String ERRO_DESCONHECIDO = "Erro desconhecido";
    private static final String SERVICO_INDISPONIVEL = "Serviço de classificação indisponível após 3 tentativas";
    private static final String RESPOSTA_CLASSIFICACAO_INVALIDA = "Resposta inválida do serviço de classificação";
    private static final String LISTA_SERIES = "lista_series";
    private static final String LISTA_FILMES = "lista_filmes";


    @Value("${CLASSIFYADDRESS}")
    private String CLASSIFYADDRESS;

    private final Gson gson = new Gson();
    private final RetryTemplate retryTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    public RequisicaoApiClassificacaoService(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    public List<ClassificacaoResponseDTO> classificarImagem(MultipartFile image) throws IOException {
        return retryTemplate.execute(
                context -> {
                    MultiValueMap<String, Object> body =
                            criarBody(image.getInputStream(), image.getOriginalFilename());

                    HttpEntity<MultiValueMap<String, Object>> requestEntity =
                            criarRequestEntity(body);

                    return requisicao(requestEntity);
                },
                context -> {
                    throw new ResponseStatusException(
                            SERVICE_UNAVAILABLE,
                            SERVICO_INDISPONIVEL,
                            context.getLastThrowable()
                    );
                }
        );
    }

    public List<ClassificacaoResponseDTO> classificarImagem(MultipartFile image,
            List<Long> idsSeries,
            List<Long> idsFilmes
    ) throws IOException {
        return retryTemplate.execute(
                context -> {
                    MultiValueMap<String, Object> body =
                            criarBody(image.getInputStream(), image.getOriginalFilename());

                    body.add(LISTA_SERIES, gson.toJson(idsSeries));
                    body.add(LISTA_FILMES, gson.toJson(idsFilmes));

                    return requisicao(criarRequestEntity(body));
                },
                context -> {
                    throw new ResponseStatusException(
                            SERVICE_UNAVAILABLE,
                            SERVICO_INDISPONIVEL,
                            context.getLastThrowable()
                    );
                }
        );
    }

    private MultiValueMap<String, Object> criarBody(
            InputStream inputStream,
            @Nullable String originalFilename
    ) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(IMAGEM, new MultipartInputStreamFileResource(inputStream, originalFilename));
        return body;
    }

    private ResponseStatusException extrairErro(HttpClientErrorException e) {
        String erroMensagem;

        try {
            JsonObject erroJson = JsonParser.parseString(e.getResponseBodyAsString()).getAsJsonObject();

            erroMensagem = ofNullable(erroJson.get(DETALHE))
                    .map(JsonElement::getAsString)
                    .orElse(ERRO_DESCONHECIDO);

        } catch (Exception ex) {
            erroMensagem = ERRO_DESCONHECIDO;
        }

        return new ResponseStatusException(BAD_REQUEST, erroMensagem, e);
    }

    private HttpEntity<MultiValueMap<String, Object>> criarRequestEntity(
            MultiValueMap<String, Object> body
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MULTIPART_FORM_DATA);
        return new HttpEntity<>(body, headers);
    }

    private List<ClassificacaoResponseDTO> requisicao(HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        try {
            ResponseEntity<ClassificacaoImagemResponseDTO> response =
                    restTemplate.exchange(
                            CLASSIFYADDRESS,
                            POST,
                            requestEntity,
                            ClassificacaoImagemResponseDTO.class
                    );

            return ofNullable(response.getBody())
                    .map(ClassificacaoImagemResponseDTO::getResultado)
                    .orElseThrow(() -> new ResponseStatusException(
                            SERVICE_UNAVAILABLE,
                            RESPOSTA_CLASSIFICACAO_INVALIDA
                    ));

        } catch (HttpClientErrorException e) {
            throw extrairErro(e);
        }
    }
}
