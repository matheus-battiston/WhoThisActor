package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;

public class PesquisaSerieMapper {

    public static PesquisaProducaoInfoResponse toResponse(Serie serie) {
        return PesquisaProducaoInfoResponse.builder()
                .nome(serie.getTitulo())
                .imagem(serie.getImagem())
                .id(serie.getId())
                .popularidade(serie.getPopularidade())
                .build();
    }
}
