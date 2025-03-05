package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaPorNomeResponse;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;

public class PesquisaMapper {
    public static PesquisaPorNomeResponse toResponse(AtorTMDBDtoPesquisaId ator){
        return PesquisaPorNomeResponse.builder()
                .urlImagem(ator.getProfile_path())
                .nome(ator.getName())
                .id(ator.getId().intValue())
                .build();
    }
}
