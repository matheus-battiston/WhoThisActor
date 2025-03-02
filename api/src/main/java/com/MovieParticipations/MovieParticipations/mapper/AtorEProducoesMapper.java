package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;

import java.util.List;

public class AtorEProducoesMapper {
    public static AtorEProducoesResponse toResponse(List<ProducaoResponse> producoes, String nome, String url){
        return AtorEProducoesResponse.builder()
                .urlFoto(url)
                .nome(nome)
                .producoes(producoes)
                .build();
    }
}
