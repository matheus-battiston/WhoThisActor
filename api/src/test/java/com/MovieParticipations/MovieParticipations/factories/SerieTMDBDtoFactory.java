package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;

public class SerieTMDBDtoFactory {
    private static final Long ID_BREAKING_BAD = 456L;
    private static final String NOME_BREAKING_BAD = "Breaking Bad";
    private static final String NOME_ORIGINAL_BREAKING_BAD = "Breaking Bad";
    private static final String IMAGEM_POSTER_BREAKING_BAD = "/breaking-bad.jpg";
    private static final Double POPULARIDADE_BREAKING_BAD = 9.5;

    public static SerieTMDBDto getBreakingBadSerieTMDBDto() {

        return SerieTMDBDto.builder()
                .id(ID_BREAKING_BAD)
                .nome(NOME_BREAKING_BAD)
                .nomeOriginal(NOME_ORIGINAL_BREAKING_BAD)
                .imagemPoster(IMAGEM_POSTER_BREAKING_BAD)
                .popularidade(POPULARIDADE_BREAKING_BAD)
                .build();
    }
}
