package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Service
public class BuscarElencoService {
    @Value("${TMDBAPIKEY}")
    private String apiKey;

    private static final String URL_BASE = "https://api.themoviedb.org/3";
    private static final String BARRA = "/";
    private static final String MEIO_DA_URL = "/credits?api_key=";
    private static final String TV_STRING = "tv";
    private static final String MOVIE_STRING = "movie";

    private String criarUrl(Long id, TipoMidia tipo){
        String tipoMidia = tipo.equals(TipoMidia.TV) ? TV_STRING : MOVIE_STRING;
        return URL_BASE + BARRA + tipoMidia + BARRA + id + MEIO_DA_URL + apiKey;

    }

    public List<AtorTMDBDto> pesquisarElencoSerie(Long id, TipoMidia tipo) {
        RestTemplate restTemplate = new RestTemplate();
        String url = criarUrl(id, tipo);

        CastResponseDto response = restTemplate.exchange(
                url,
                GET,
                HttpEntity.EMPTY,
                CastResponseDto.class // Agora pegamos um objeto, não uma lista diretamente
        ).getBody();

// Garantir que não seja null e extrair a lista corretamente
        List<AtorTMDBDto> castList = response.getCast() != null ? response.getCast() : List.of();

        return castList.stream()
                .filter(ator -> "Acting".equalsIgnoreCase(ator.getKnown_for_department())) // Filtra apenas atores
                .toList();
    }
}
