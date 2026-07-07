package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.dto.BuscarIdFilmePorNomeDTO;
import com.MovieParticipations.MovieParticipations.dto.BuscarIdSeriePorNomeDTO;
import com.MovieParticipations.MovieParticipations.dto.FilmeDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.SerieDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.util.NormalizadorDeString.normalizar;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class BuscarProducaoPorNomeTMBDService {
    @Value(value = "${TMDBAPIKEY}")
    private String apiKey;
    private static final String SERIE_NAO_ENCONTRADA = "Serie nao foi encontrada, verifique o nome e tente novamente";
    private static final String FILME_NAO_ENCONTRADO = "Filme nao foi encontrado, verifique o nome e tente novamente";
    private static final String TMDB_SEARCH_URL = "https://api.themoviedb.org/3/search";
    private static final String TMDB_DETAILS_URL = "https://api.themoviedb.org/3";
    private static final String MOVIE = "/movie";
    private static final String TV = "/tv";
    private static final String QUERY = "query";
    private static final String API_KEY = "api_key";

    private final RestTemplate restTemplate;

    public BuscarProducaoPorNomeTMBDService() {
        this.restTemplate = new RestTemplate();
    }

    public List<FilmeTMDBDto> buscarIdFilmePorNome(String nome) {
        String url = UriComponentsBuilder.fromUriString(TMDB_SEARCH_URL + MOVIE)
                .queryParam(QUERY, nome)
                .queryParam(API_KEY, apiKey)
                .toUriString();
        return buscarFilme(url, nome);
    }

    public List<SerieTMDBDto> buscarIdSeriePorNome(String nome) {
        String url = UriComponentsBuilder.fromUriString(TMDB_SEARCH_URL + TV)
                .queryParam(QUERY, nome)
                .queryParam(API_KEY, apiKey)
                .toUriString();
        return buscarSerie(url, nome);
    }

    public FilmeDetalhesTMDBDto buscarDetalhesFilmePorId(Long idTmdb) {
        String url = UriComponentsBuilder.fromUriString(TMDB_DETAILS_URL + MOVIE + "/" + idTmdb)
                .queryParam(API_KEY, apiKey)
                .toUriString();

        return restTemplate.exchange(
                url, GET, HttpEntity.EMPTY, FilmeDetalhesTMDBDto.class
        ).getBody();
    }

    public SerieDetalhesTMDBDto buscarDetalhesSeriePorId(Long idTmdb) {
        String url = UriComponentsBuilder.fromUriString(TMDB_DETAILS_URL + TV + "/" + idTmdb)
                .queryParam(API_KEY, apiKey)
                .toUriString();

        return restTemplate.exchange(
                url, GET, HttpEntity.EMPTY, SerieDetalhesTMDBDto.class
        ).getBody();
    }

    private List<FilmeTMDBDto> buscarFilme(String url, String nome) {
        BuscarIdFilmePorNomeDTO response = restTemplate.exchange(
                url, GET, HttpEntity.EMPTY, BuscarIdFilmePorNomeDTO.class
        ).getBody();

        if (response != null && response.getResultados() != null && !response.getResultados().isEmpty()) {
            List<FilmeTMDBDto> nomeIgual = temNomeExatoFilme(response, nome);
            if (!nomeIgual.isEmpty()) return nomeIgual;
        }

        throw new ResponseStatusException(BAD_REQUEST, FILME_NAO_ENCONTRADO);

    }

    private List<SerieTMDBDto> buscarSerie(String url, String nome) throws ResponseStatusException {
        BuscarIdSeriePorNomeDTO response = restTemplate.exchange(
                url, GET, HttpEntity.EMPTY, BuscarIdSeriePorNomeDTO.class
        ).getBody();

        if (response != null && response.getResultados() != null && !response.getResultados().isEmpty()) {
            List<SerieTMDBDto> nomeIgual = temNomeExatoSerie(response, nome);
            if (!nomeIgual.isEmpty()) return nomeIgual;
        }

        throw new ResponseStatusException(BAD_REQUEST, SERIE_NAO_ENCONTRADA);
    }

    private boolean nomeExato(String pesquisaUsuario, String nomeOficial) {
        if (pesquisaUsuario == null || nomeOficial == null) return false;
        return normalizar(pesquisaUsuario).equals(normalizar(nomeOficial));
    }

    private List<FilmeTMDBDto> temNomeExatoFilme(BuscarIdFilmePorNomeDTO response, String nome) {
        return response.getResultados().stream()
                .filter(filme -> nomeExato(nome, filme.getTituloOriginal())).toList();
    }


    private List<SerieTMDBDto> temNomeExatoSerie(BuscarIdSeriePorNomeDTO response, String nome) {
        return response.getResultados().stream()
                .filter(serie -> nomeExato(nome, serie.getNomeOriginal())).toList();
    }

}
