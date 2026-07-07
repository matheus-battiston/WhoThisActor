package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;

import java.time.LocalDate;

public class FilmeTMDBDtoFactory {
    private static final Long ID_MATRIX = 123L;
    private static final String TITULO_MATRIX = "Matrix";
    private static final String TITULO_ORIGINAL_MATRIX = "The Matrix";
    private static final String IMAGEM_POSTER_MATRIX = "/matrix.jpg";
    private static final Double POPULARIDADE_MATRIX = 10.0;
    private static final int ANO_LANCAMENTO_MATRIX = 2011;
    private static final int MES_LANCAMENTO_MATRIX = 2;
    private static final int DIA_LANCAMENTO_MATRIX = 10;
    private static final LocalDate DATA_LANCAMENTO_MATRIX = LocalDate.of(
            ANO_LANCAMENTO_MATRIX,
            MES_LANCAMENTO_MATRIX,
            DIA_LANCAMENTO_MATRIX
    );
    private static final String TITULO_MATRIX_REVOLUTIONS = "Matrix Revolutions";
    private static final String IMAGEM_POSTER_MATRIX_REVOLUTIONS = "/matrix-revolutions.jpg";
    private static final Double POPULARIDADE_MATRIX_REVOLUTIONS = 20.0;
    private static final int ANO_LANCAMENTO_MATRIX_REVOLUTIONS = 2021;
    private static final int MES_LANCAMENTO_MATRIX_REVOLUTIONS = 12;
    private static final int DIA_LANCAMENTO_MATRIX_REVOLUTIONS = 22;
    private static final LocalDate DATA_LANCAMENTO_MATRIX_REVOLUTIONS = LocalDate.of(
            ANO_LANCAMENTO_MATRIX_REVOLUTIONS,
            MES_LANCAMENTO_MATRIX_REVOLUTIONS,
            DIA_LANCAMENTO_MATRIX_REVOLUTIONS
    );

    public static FilmeTMDBDto getMatrixFilmeTMDBDto() {

        return FilmeTMDBDto.builder()
                .id(ID_MATRIX)
                .titulo(TITULO_MATRIX)
                .tituloOriginal(TITULO_ORIGINAL_MATRIX)
                .imagemPoster(IMAGEM_POSTER_MATRIX)
                .popularidade(POPULARIDADE_MATRIX)
                .dataLancamento(DATA_LANCAMENTO_MATRIX)
                .build();
    }

    public static FilmeTMDBDto getMatrixRevolutionsFilmeTMDBDto() {
        return FilmeTMDBDto.builder()
                .id(ID_MATRIX)
                .titulo(TITULO_MATRIX_REVOLUTIONS)
                .tituloOriginal(TITULO_MATRIX_REVOLUTIONS)
                .imagemPoster(IMAGEM_POSTER_MATRIX_REVOLUTIONS)
                .popularidade(POPULARIDADE_MATRIX_REVOLUTIONS)
                .dataLancamento(DATA_LANCAMENTO_MATRIX_REVOLUTIONS)
                .build();
    }

    public static FilmeTMDBDto getFilmeTMDBDtoSemId() {
        return FilmeTMDBDto.builder()
                .titulo(TITULO_MATRIX)
                .tituloOriginal(TITULO_ORIGINAL_MATRIX)
                .imagemPoster(IMAGEM_POSTER_MATRIX)
                .popularidade(POPULARIDADE_MATRIX)
                .dataLancamento(DATA_LANCAMENTO_MATRIX)
                .build();
    }

}
