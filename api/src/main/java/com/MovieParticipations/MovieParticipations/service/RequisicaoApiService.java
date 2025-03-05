package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.dto.ListaProducoesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.PesquisaIdPorNomeDTO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpMethod.GET;

@Service
public class RequisicaoApiService {

    private static final String QUERY = "&query=";
    private static final String URL_PESQUISA_POR_NOME = "https://api.themoviedb.org/3/search/person";
    private static final String PARAMETRO_API_KEY = "?api_key=";
    private static final String URL_PESQUISA_CREDITOS_COMBINADOS_POR_ID = "https://api.themoviedb.org/3/person";
    private static final String PARAMETRO_CREDITOS_COMBINADOS = "/combined_credits";
    private static final String BARRA = "/";

    @Value("${TMDBAPIKEY}")
    private String apiKey;
    public PesquisaIdPorNomeDTO persquisarIdPorNome(String nomeAtor) {
        String nomeCodificado = URLEncoder.encode(nomeAtor, StandardCharsets.UTF_8);
        String url = URL_PESQUISA_POR_NOME + PARAMETRO_API_KEY + apiKey + QUERY + nomeCodificado;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, GET, HttpEntity.EMPTY, PesquisaIdPorNomeDTO.class).getBody();
    }

    public ListaProducoesTMDBDto pesquisarFilmesDoAtorPorId(int id) {
        String url = URL_PESQUISA_CREDITOS_COMBINADOS_POR_ID +
                BARRA + id + PARAMETRO_CREDITOS_COMBINADOS + PARAMETRO_API_KEY + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, GET, HttpEntity.EMPTY, ListaProducoesTMDBDto.class).getBody();
    }
}
