package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.BuscarProvidersDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Service
public class ProvidersService {
    @Value(value = "${TMDBAPIKEY}")
    private String apiKey;
    private static final String URL_BASE = "https://api.themoviedb.org/3";
    private static final String PESQUISA = "/watch/providers?api_key=";
    private static final String MOVIE = "/movie";
    private static final String TV = "/tv";
    private static final String BARRA = "/";
    private final RestTemplate restTemplate;
    private final static String URL_BRASIL = "BR";

    public ProvidersService() {this.restTemplate = new RestTemplate();}

    public List<ProviderDto> buscarProvidersDto(Long id, TipoMidia tipoMidia){
        String url = gerarUrl(id, tipoMidia);
        BuscarProvidersDto buscarProvidersDto = restTemplate.exchange(url, GET, HttpEntity.EMPTY, BuscarProvidersDto.class)
                .getBody();

        if (buscarProvidersDto.getResults().get(URL_BRASIL) == null || buscarProvidersDto.getResults().get(URL_BRASIL).getFlatrate() == null)
            return null;
        return buscarProvidersDto.getResults().get(URL_BRASIL).getFlatrate();
    }

    private String gerarUrl(Long id, TipoMidia tipoMidia){
        String tipo = tipoMidia.equals(TipoMidia.TV) ? TV : MOVIE;
        return URL_BASE + tipo + BARRA + id + PESQUISA + apiKey;
    }
}
