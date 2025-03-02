package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaPorNomeResponse;

public class PesquisaMapper {
    public static PesquisaPorNomeResponse toResponse(int id, String nome, String urlImage){
        return PesquisaPorNomeResponse.builder()
                .nome(nome)
                .id(id)
                .urlImagem(urlImage)
                .build();
    }
}
