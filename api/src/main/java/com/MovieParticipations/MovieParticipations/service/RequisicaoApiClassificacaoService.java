package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.mapper.ClassificacaoApiExternaMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class RequisicaoApiClassificacaoService {
    @Value("${CLASSIFYADDRESS}")
    private String CLASSIFYADDRESS;
    public JsonArray classificarImagem(String urlImagem, String nomeSerie) {
        RestTemplate restTemplate = new RestTemplate();
        String body = ClassificacaoApiExternaMapper.toResponse(urlImagem, nomeSerie);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            String response = restTemplate.postForObject(CLASSIFYADDRESS, request, String.class);
            return JsonParser.parseString(response).getAsJsonArray();
        } catch (HttpClientErrorException e) {
            JsonObject erroJson = JsonParser.parseString(e.getResponseBodyAsString()).getAsJsonObject();
            String erroMensagem = Optional.ofNullable(erroJson.get("detail"))
                    .map(JsonElement::getAsString)
                    .orElse("Erro desconhecido");
            throw new ResponseStatusException(BAD_REQUEST, erroMensagem);
        }

    }
}
