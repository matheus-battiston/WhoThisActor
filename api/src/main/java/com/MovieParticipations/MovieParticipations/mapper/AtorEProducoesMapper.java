package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.AtorEProducoesResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoAtorResponse;

public class AtorEProducoesMapper {
    public static AtorEProducoesResponse toResponse(ProducaoAtorResponse producoes, String nome, String url, Long id, Boolean estaFavoritado){
        return AtorEProducoesResponse.builder()
                .urlFoto(url)
                .nome(nome)
                .producoes(producoes)
                .id(id)
                .favoritado(estaFavoritado)
                .build();
    }
}
