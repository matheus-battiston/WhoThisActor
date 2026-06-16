package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;

public class SerieTMDBDtoFactory {

    public static SerieTMDBDto getBreakingBad() {
        return SerieTMDBDto.builder()
                .id(456L)
                .nome("Breaking Bad")
                .nomeOriginal("Breaking Bad")
                .imagemPoster("/breaking-bad.jpg")
                .popularidade(9.5)
                .build();
    }
}
