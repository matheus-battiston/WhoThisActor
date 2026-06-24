package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoAtorResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;

import java.util.List;

public class ProducaoesAtorMapper {
    public static ProducaoAtorResponse toResponse(List<ProducaoComPersonagemResponse> filmes, List<ProducaoComPersonagemResponse> series) {
        return ProducaoAtorResponse.builder()
                .series(series)
                .filmes(filmes)
                .build();
    }
}
