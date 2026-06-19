package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoComPersonagemResponse;

public class ProducaoComPersonagemResponseFactory {
    private static final Long ID_MATRIX = 1L;
    private static final String NOME_PRODUCAO_MATRIX = "Matrix";
    private static final String NOME_PERSONAGEM_NEO = "Neo";
    private static final String POSTER_LINK_MATRIX = "/matrix.jpg";
    private static final Long ID_BREAKING_BAD = 2L;
    private static final String NOME_PRODUCAO_BREAKING_BAD = "Breaking Bad";
    private static final String NOME_PERSONAGEM_WALTER_WHITE = "Walter White";
    private static final String POSTER_LINK_BREAKING_BAD = "/breaking-bad.jpg";

    public static ProducaoComPersonagemResponse getMatrixProducaoComPersonagemResponse() {

        return ProducaoComPersonagemResponse.builder()
                .id(ID_MATRIX)
                .nomeProducao(NOME_PRODUCAO_MATRIX)
                .nomePersonagem(NOME_PERSONAGEM_NEO)
                .posterLink(POSTER_LINK_MATRIX)
                .build();
    }

    public static ProducaoComPersonagemResponse getBreakingBadProducaoComPersonagemResponse() {

        return ProducaoComPersonagemResponse.builder()
                .id(ID_BREAKING_BAD)
                .nomeProducao(NOME_PRODUCAO_BREAKING_BAD)
                .nomePersonagem(NOME_PERSONAGEM_WALTER_WHITE)
                .posterLink(POSTER_LINK_BREAKING_BAD)
                .build();
    }
}
