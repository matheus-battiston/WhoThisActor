package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.BuscarIdFilmePorNomeDTO;
import com.MovieParticipations.MovieParticipations.dto.BuscarIdSeriePorNomeDTO;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDto;
import com.MovieParticipations.MovieParticipations.dto.TMDBDto;
import com.MovieParticipations.MovieParticipations.mapper.TMDBDtoMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Service
public class BuscarProducaoPorNomeTMBDService {
    @Value("${tmdb.api.key}")
    private String apiKey;
    private static final String SERIE_NAO_ENCONTRADA = "Serie nao foi encontrada, verifique o nome";
    private static final String TMDB_SEARCH_URL = "https://api.themoviedb.org/3/search";
    private static final String MOVIE = "/movie";
    private static final String TV = "/tv";
    private final RestTemplate restTemplate;

    public BuscarProducaoPorNomeTMBDService() {
        this.restTemplate = new RestTemplate();
    }

    public TMDBDto buscarIdPorNome(String nome, TipoMidia tipo) {
        String tipoString = tipo.equals(TipoMidia.TV) ? TV : MOVIE;
        String url = UriComponentsBuilder.fromUriString(TMDB_SEARCH_URL + tipoString)
                .queryParam("query", nome)
                .queryParam("api_key", apiKey)
                .toUriString();

        if (tipo.equals(TipoMidia.TV))
            return buscarSerie(url, tipo);
         else
             return buscarFilme(url, tipo);
        }

    private TMDBDto buscarSerie(String url, TipoMidia tipo){
        BuscarIdSeriePorNomeDTO response = restTemplate.exchange(
                url, GET, HttpEntity.EMPTY, BuscarIdSeriePorNomeDTO.class
        ).getBody();

        if (response.getSeries() != null && !response.getSeries().isEmpty()) {
            response.getSeries().sort((serie1, serie2) ->
                    Double.compare(serie2.getPopularity(), serie1.getPopularity())
            );
            return TMDBDtoMapper.toTMDBDtoFromSerie(response.getSeries().get(0), tipo);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERIE_NAO_ENCONTRADA);
        }
    }

    private TMDBDto buscarFilme(String url, TipoMidia tipo){
        BuscarIdFilmePorNomeDTO response = restTemplate.exchange(
                url, GET, HttpEntity.EMPTY, BuscarIdFilmePorNomeDTO.class
        ).getBody();

        if (response.getFilmes() != null && !response.getFilmes().isEmpty()) {
            response.getFilmes().sort((filme1, filme2) ->
                    Double.compare(filme2.getPopularity(), filme1.getPopularity())
            );
            return TMDBDtoMapper.toTMDBDtoFromFilme(response.getFilmes().get(0), tipo);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERIE_NAO_ENCONTRADA);
        }
    }
}
