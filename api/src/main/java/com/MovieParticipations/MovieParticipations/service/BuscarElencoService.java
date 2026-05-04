package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDtoMovie;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDtoSerie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.MOVIE;
import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.TV;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;

@Service
public class BuscarElencoService {
    @Value("${TMDBAPIKEY}")
    private String apiKey;
    private static final String URL_BASE = "https://api.themoviedb.org/3";
    private static final String BARRA = "/";
    private static final String MEIO_DA_URL_FILME = "/credits?api_key=";
    private static final String MEIO_DA_URL_SERIE = "/aggregate_credits?api_key=";
    private static final String TV_STRING = "tv";
    private static final String MOVIE_STRING = "movie";
    private static final String ATUANDO = "Acting";

    private String criarUrl(Long id, TipoMidia tipo){
        String tipoMidia = tipo.equals(TV) ? TV_STRING : MOVIE_STRING;
        String MEIO_DA_URL = tipo.equals(TV) ? MEIO_DA_URL_SERIE : MEIO_DA_URL_FILME;
        return URL_BASE + BARRA + tipoMidia + BARRA + id + MEIO_DA_URL + apiKey;
    }

    public List<AtorTMDBSerieDto> pesquisaElencoSerie(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = criarUrl(id, TV);
        CastResponseDtoSerie response = restTemplate.exchange(
                url,
                GET,
                EMPTY,
                CastResponseDtoSerie.class
        ).getBody();

        return ofNullable(response)
                .map(CastResponseDtoSerie::getElenco)
                .orElse(of())
                .stream()
                .filter(ator -> ATUANDO.equalsIgnoreCase(ator.getConhecidoPor()))
                .toList();
    }

    public List<AtorTMDBMovieDto> pesquisaElencoFilme(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = criarUrl(id, MOVIE);
        CastResponseDtoMovie response = restTemplate.exchange(
                url,
                GET,
                EMPTY,
                CastResponseDtoMovie.class
        ).getBody();

        return ofNullable(response)
                .map(CastResponseDtoMovie::getElenco)
                .orElse(of())
                .stream()
                .filter(ator -> ATUANDO.equalsIgnoreCase(ator.getConhecidoPor()))
                .toList();
    }
}
