package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;

public class FilmeTMDBDtoFactory {

    public static FilmeTMDBDto getMatrix() {
        return FilmeTMDBDto.builder()
                .id(123L)
                .titulo("Matrix")
                .tituloOriginal("The Matrix")
                .imagemPoster("/matrix.jpg")
                .popularidade(10.0)
                .build();
    }
}
