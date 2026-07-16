package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.FilmeDetalhesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.GeneroTMDBDto;

import java.time.LocalDate;
import java.util.List;

public class FilmeDetalhesTMDBDtoFactory {
    private static final Long ID_MATRIX = 123L;
    private static final String TITULO_MATRIX = "Matrix";
    private static final String TITULO_ORIGINAL_MATRIX = "The Matrix";
    private static final String IMAGEM_POSTER_MATRIX = "/matrix.jpg";
    private static final String BACKDROP_MATRIX = "/matrix-backdrop.jpg";
    private static final Double POPULARIDADE_MATRIX = 10.0;
    private static final int ANO_LANCAMENTO_MATRIX = 2011;
    private static final int MES_LANCAMENTO_MATRIX = 2;
    private static final int DIA_LANCAMENTO_MATRIX = 10;
    private static final LocalDate DATA_LANCAMENTO_MATRIX = LocalDate.of(
            ANO_LANCAMENTO_MATRIX,
            MES_LANCAMENTO_MATRIX,
            DIA_LANCAMENTO_MATRIX
    );
    private static final Long ID_GENERO_ACAO = 28L;
    private static final String GENERO_ACAO = "Action";
    private static final String OVERVIEW_MATRIX = "Um hacker descobre a verdade sobre sua realidade.";

    public static FilmeDetalhesTMDBDto getMatrixFilmeDetalhesTMDBDto() {
        return FilmeDetalhesTMDBDto.builder()
                .id(ID_MATRIX)
                .titulo(TITULO_MATRIX)
                .tituloOriginal(TITULO_ORIGINAL_MATRIX)
                .imagemPoster(IMAGEM_POSTER_MATRIX)
                .backdropPath(BACKDROP_MATRIX)
                .popularidade(POPULARIDADE_MATRIX)
                .dataLancamento(DATA_LANCAMENTO_MATRIX)
                .genres(List.of(GeneroTMDBDto.builder().id(ID_GENERO_ACAO).name(GENERO_ACAO).build()))
                .overview(OVERVIEW_MATRIX)
                .build();
    }

    public static FilmeDetalhesTMDBDto getMatrixFilmeDetalhesSemGenerosTMDBDto() {
        FilmeDetalhesTMDBDto detalhes = getMatrixFilmeDetalhesTMDBDto();
        detalhes.setGenres(List.of());
        return detalhes;
    }

    public static FilmeDetalhesTMDBDto getMatrixFilmeDetalhesComGenerosNulosTMDBDto() {
        FilmeDetalhesTMDBDto detalhes = getMatrixFilmeDetalhesTMDBDto();
        detalhes.setGenres(null);
        return detalhes;
    }
}
