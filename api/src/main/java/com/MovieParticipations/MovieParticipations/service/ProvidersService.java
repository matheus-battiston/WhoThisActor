package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.BuscarProvidersDto;
import com.MovieParticipations.MovieParticipations.dto.CountryProviderDto;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpEntity.EMPTY;
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

    public ProvidersService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(cacheNames = "providers", key = "#tipo.name() + ':' + #id")
    public List<ProviderDto> buscarProviders(Long id, TipoMidia tipo) {
        String url = gerarUrl(id, tipo);
        BuscarProvidersDto buscarProvidersDto = restTemplate.exchange(url, GET, EMPTY, BuscarProvidersDto.class)
                .getBody();

        if (buscarProvidersDto == null || buscarProvidersDto.getResultados() == null)
            return null;

        CountryProviderDto providersBrasil = buscarProvidersDto.getResultados().get(URL_BRASIL);
        if (providersBrasil == null || providersBrasil.getFlatrate() == null)
            return null;

        return providersBrasil.getFlatrate();
    }

    private String gerarUrl(Long id, TipoMidia tipo){
        return switch (tipo) {
            case TV -> URL_BASE + TV + BARRA + id + PESQUISA + apiKey;
            case MOVIE -> URL_BASE + MOVIE + BARRA + id + PESQUISA + apiKey;
        };

    }
}
