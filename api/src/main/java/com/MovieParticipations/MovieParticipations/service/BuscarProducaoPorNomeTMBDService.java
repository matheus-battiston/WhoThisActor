package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.*;
import com.MovieParticipations.MovieParticipations.mapper.TMDBDtoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpMethod.GET;

@Service
public class BuscarProducaoPorNomeTMBDService {
    @Value(value = "${TMDBAPIKEY}")
    private String apiKey;
    private static final String SERIE_NAO_ENCONTRADA = "Serie nao foi encontrada, verifique o nome e tente novamente";
    private static final String FILME_NAO_ENCONTRADO = "Filme nao foi encontrado, verifique o nome e tente novamente";
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
            return buscarSerie(url, tipo, nome);
         else
             return buscarFilme(url, tipo, nome);
        }

    private TMDBDto buscarFilme(String url, TipoMidia tipo, String nome){
        BuscarIdFilmePorNomeDTO response = restTemplate.exchange(
                url, GET, HttpEntity.EMPTY, BuscarIdFilmePorNomeDTO.class
        ).getBody();

        if (response.getResults() != null && !response.getResults().isEmpty()) {
            FilmeTMDBDto nomeIgual = temNomeExatoFilme(response, nome);
            if (nomeIgual != null)
                return TMDBDtoMapper.toTMDBDtoFromFilme(nomeIgual, tipo);
            else
                return TMDBDtoMapper.toTMDBDtoFromFilme(getNomeFilmeParecidoPorPopularidade(response), tipo);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FILME_NAO_ENCONTRADO);
        }
    }

    private TMDBDto buscarSerie(String url, TipoMidia tipo, String nome){
        BuscarIdSeriePorNomeDTO response = restTemplate.exchange(
                url, GET, HttpEntity.EMPTY, BuscarIdSeriePorNomeDTO.class
        ).getBody();

        if (response.getResults() != null && !response.getResults().isEmpty()) {
            SerieTMDBDto nomeIgual = temNomeExatoSerie(response, nome);
            if (nomeIgual != null)
                return TMDBDtoMapper.toTMDBDtoFromSerie(nomeIgual, tipo);
            else
                return TMDBDtoMapper.toTMDBDtoFromSerie(getNomeSerieParecidoPorPopularidade(response), tipo);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERIE_NAO_ENCONTRADA);
        }
    }

    private boolean nomeExato(String pesquisaUsuario, String nomeOficial){
        return pesquisaUsuario.equalsIgnoreCase(nomeOficial);
    }

    private FilmeTMDBDto temNomeExatoFilme(BuscarIdFilmePorNomeDTO response, String nome){
        return response.getResults().stream()
                .filter(serie -> nomeExato(nome, serie.getTitle()))
                .toList()
                .get(0);
    }

    private SerieTMDBDto temNomeExatoSerie(BuscarIdSeriePorNomeDTO response, String nome){
        return response.getResults().stream()
                .filter(serie -> nomeExato(nome, serie.getName()))
                .toList()
                .get(0);
    }


    private FilmeTMDBDto getNomeFilmeParecidoPorPopularidade(BuscarIdFilmePorNomeDTO response){
        response.getResults().sort((filme1, filme2) ->
                Double.compare(filme2.getPopularity(), filme1.getPopularity())
        );
        return response.getResults().get(0);
    }

    private SerieTMDBDto getNomeSerieParecidoPorPopularidade(BuscarIdSeriePorNomeDTO response){
        response.getResults().sort((filme1, filme2) ->
                Double.compare(filme2.getPopularity(), filme1.getPopularity())
        );
        return response.getResults().get(0);
    }
}
