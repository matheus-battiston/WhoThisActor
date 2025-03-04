package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDtoMovie;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDtoSerie;
import com.MovieParticipations.MovieParticipations.mapper.AtorTMDBDtoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.*;
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

    private String criarUrl(Long id, TipoMidia tipo){
        String tipoMidia = tipo.equals(TV) ? TV_STRING : MOVIE_STRING;
        String MEIO_DA_URL = tipo.equals(TV) ? MEIO_DA_URL_SERIE : MEIO_DA_URL_FILME;
        return URL_BASE + BARRA + tipoMidia + BARRA + id + MEIO_DA_URL + apiKey;

    }

    public List<AtorTMDBDto> pesquisarElencoFilme(Long id){
        RestTemplate restTemplate = new RestTemplate();
        String url = criarUrl(id, MOVIE);

        CastResponseDtoMovie response = restTemplate.exchange(
                url,
                GET,
                HttpEntity.EMPTY,
                CastResponseDtoMovie.class
        ).getBody();

        List<AtorTMDBDto> castList = response.getCast() != null ? response.getCast().stream()
                .map(AtorTMDBDtoMapper::toDtoFromMovie)
                .toList() : List.of();

        return castList.stream()
                .filter(ator -> "Acting".equalsIgnoreCase(ator.getKnown_for_department()))
                .toList();
    }

    public List<AtorTMDBDto> pesquisarElenco(Long id, TipoMidia tipo) {
        if (tipo.equals(MOVIE))
            return pesquisarElencoFilme(id);

        return pesquisaElencoSerie(id);
    }

    private List<AtorTMDBDto> pesquisaElencoSerie(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = criarUrl(id, TV);

        CastResponseDtoSerie response = restTemplate.exchange(
                url,
                GET,
                HttpEntity.EMPTY,
                CastResponseDtoSerie.class
        ).getBody();


        List<AtorTMDBDto> castList = response.getCast() != null ? response.getCast().stream()
                .map(AtorTMDBDtoMapper::toDtoFromSerie)
                .toList() : List.of();

        return castList.stream()
                .filter(ator -> "Acting".equalsIgnoreCase(ator.getKnown_for_department()))
                .toList();
    }
}
