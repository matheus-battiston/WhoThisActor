package com.MovieParticipations.MovieParticipations.service;


import com.MovieParticipations.MovieParticipations.controller.response.ProvedoresResponse;
import com.MovieParticipations.MovieParticipations.domain.Provedores;
import com.MovieParticipations.MovieParticipations.domain.TipoProducao;
import com.MovieParticipations.MovieParticipations.mapper.ProvedorMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetProvedoresService {
    @Value("${tmdb.api.key}")
    private String apiKey;

    private static final String URL_BASE = "https://api.themoviedb.org/3";
    private static final String BARRA = "/";
    private static final String MEIO_URL = "/watch/providers?api_key=";
    private static final String FLATRATE = "flatrate";
    private static final String BRASIL = "BR";
    private static final String RESULTS = "results";
    private static final String PARAMETRO_PROVDEOR = "provider_name";
    private static final String STRING_NETFLIX = "Netflix";
    private static final String STRING_DISNEY = "Disney Plus";
    private static final String STRING_PRIME = "Amazon Prime Video";
    private static final String STRING_MAX = "Max";
    private static final String STRING_TV = "tv";
    private static final String STRING_FILME = "movie";



    public List<ProvedoresResponse> getProvedores (TipoProducao tipo, int id){
        String stringTipo = "";

        switch (tipo){
            case TV -> stringTipo = STRING_TV;
            case FILME -> stringTipo = STRING_FILME;
        }

        String url = URL_BASE + BARRA +
                stringTipo + BARRA + id + MEIO_URL + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        List<Provedores> provedores = new ArrayList<>();
        List<ProvedoresResponse> listaProvedores = new ArrayList<>();

        try{
            String response = restTemplate.getForObject(url, String.class);
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            JsonObject results = jsonResponse.getAsJsonObject(RESULTS);
            if (results != null) {
                JsonArray brazilProviders =
                        results.getAsJsonObject(BRASIL)
                                .getAsJsonArray(FLATRATE);

                for (var provider : brazilProviders){
                    JsonObject objetoProvedor = provider.getAsJsonObject();
                    String provedorString = objetoProvedor.get(PARAMETRO_PROVDEOR).getAsString();
                    switch (provedorString) {
                        case STRING_NETFLIX -> provedores.add(Provedores.NETFLIX);
                        case STRING_DISNEY -> provedores.add(Provedores.DISNEY);
                        case STRING_MAX -> provedores.add(Provedores.MAX);
                        case STRING_PRIME -> provedores.add(Provedores.PRIME);
                    }
                }
            }
            } catch (Exception ignored){}

        for (Provedores provedor : provedores){
            listaProvedores.add(ProvedorMapper.toResponse(provedor));
        }
        return listaProvedores;
    }
}
