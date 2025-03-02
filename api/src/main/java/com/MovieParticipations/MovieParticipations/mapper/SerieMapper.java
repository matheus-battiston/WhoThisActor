package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.SerieInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.TMDBDto;

public class SerieMapper {
    public static Serie toEntity(TMDBDto dto) {
        return Serie.builder()
                .imagem(dto.getImage())
                .titulo(dto.getName())
                .tipo(dto.getTipoProducao())
                .build();
    }

    public static SerieInfoResponse toResponse(Serie serie) {
        return SerieInfoResponse.builder()
                .nome(serie.getTitulo())
                .tipoMidia(serie.getTipo())
                .imagem(serie.getImagem())
                .build();
    }
}
