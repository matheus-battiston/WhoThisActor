package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;

public class ProducaoComPersonagemResponseFactory {

    public static ProducaoComPersonagemResponse getMatrix() {
        return ProducaoComPersonagemResponse.builder()
                .id(1L)
                .nomeProducao("Matrix")
                .nomePersonagem("Neo")
                .posterLink("/matrix.jpg")
                .build();
    }

    public static ProducaoComPersonagemResponse getBreakingBad() {
        return ProducaoComPersonagemResponse.builder()
                .id(2L)
                .nomeProducao("Breaking Bad")
                .nomePersonagem("Walter White")
                .posterLink("/breaking-bad.jpg")
                .build();
    }
}
