package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;

public class ProducaoMapper {
    public static ProducaoResponse toResponse(Serie serie) {
        return ProducaoResponse.builder()
                .nome(serie.getTitulo())
                .imagem(serie.getImagem())
                .id(serie.getId())
                .build();
    }

    public static ProducaoResponse toResponse(Filme filme) {
        return ProducaoResponse.builder()
                .nome(filme.getTitulo())
                .imagem(filme.getImagem())
                .id(filme.getId())
                .build();
    }
}
