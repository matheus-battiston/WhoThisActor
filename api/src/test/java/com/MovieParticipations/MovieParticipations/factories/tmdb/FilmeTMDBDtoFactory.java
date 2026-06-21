package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;

public class FilmeTMDBDtoFactory {
    private static final Long ID_MATRIX = 123L;
    private static final String TITULO_MATRIX = "Matrix";
    private static final String TITULO_ORIGINAL_MATRIX = "The Matrix";
    private static final String IMAGEM_POSTER_MATRIX = "/matrix.jpg";
    private static final Double POPULARIDADE_MATRIX = 10.0;
    private static final String TITULO_MATRIX_REVOLUTIONS = "Matrix Revolutions";
    private static final String IMAGEM_POSTER_MATRIX_REVOLUTIONS = "/matrix-revolutions.jpg";
    private static final Double POPULARIDADE_MATRIX_REVOLUTIONS = 20.0;

    public static FilmeTMDBDto getMatrixFilmeTMDBDto() {

        return FilmeTMDBDto.builder()
                .id(ID_MATRIX)
                .titulo(TITULO_MATRIX)
                .tituloOriginal(TITULO_ORIGINAL_MATRIX)
                .imagemPoster(IMAGEM_POSTER_MATRIX)
                .popularidade(POPULARIDADE_MATRIX)
                .build();
    }

    public static FilmeTMDBDto getMatrixRevolutionsFilmeTMDBDto() {
        return FilmeTMDBDto.builder()
                .id(ID_MATRIX)
                .titulo(TITULO_MATRIX_REVOLUTIONS)
                .tituloOriginal(TITULO_MATRIX_REVOLUTIONS)
                .imagemPoster(IMAGEM_POSTER_MATRIX_REVOLUTIONS)
                .popularidade(POPULARIDADE_MATRIX_REVOLUTIONS)
                .build();
    }

    public static FilmeTMDBDto getFilmeTMDBDtoSemId() {
        return FilmeTMDBDto.builder()
                .titulo(TITULO_MATRIX)
                .tituloOriginal(TITULO_ORIGINAL_MATRIX)
                .imagemPoster(IMAGEM_POSTER_MATRIX)
                .popularidade(POPULARIDADE_MATRIX)
                .build();
    }

}
