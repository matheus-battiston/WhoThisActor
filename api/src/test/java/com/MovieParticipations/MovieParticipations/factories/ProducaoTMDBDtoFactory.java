package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;

public class ProducaoTMDBDtoFactory {

    public static ProducaoTMDBDto getMatrix() {
        return ProducaoTMDBDto.builder()
                .id(123L)
                .titulo("Matrix")
                .tipoMidia("movie")
                .popularidade(10.0)
                .imagemPoster("/matrix.jpg")
                .personagem("Neo")
                .ordem(1L)
                .build();
    }

    public static ProducaoTMDBDto getBreakingBad() {
        return ProducaoTMDBDto.builder()
                .id(456L)
                .nome("Breaking Bad")
                .tipoMidia("tv")
                .popularidade(9.5)
                .imagemPoster("/breaking-bad.jpg")
                .personagem("Walter White")
                .numeroEpisodios(62)
                .ordem(1L)
                .build();
    }
}
