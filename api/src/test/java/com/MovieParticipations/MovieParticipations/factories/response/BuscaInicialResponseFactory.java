package com.MovieParticipations.MovieParticipations.factories.response;

import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialPessoaResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialProducaoResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialResponse;

import static java.util.List.of;

public class BuscaInicialResponseFactory {
    private static final Long ID_KEANU_REEVES = 99L;
    private static final String NOME_KEANU_REEVES = "Keanu Reeves";
    private static final String URL_IMAGEM_KEANU_REEVES = "https://image.tmdb.org/t/p/w400/keanu.jpg";
    private static final Long ID_BREAKING_BAD = 2L;
    private static final String NOME_PRODUCAO_BREAKING_BAD = "Breaking Bad";
    private static final String URL_IMAGEM_BREAKING_BAD = "https://image.tmdb.org/t/p/w400/breaking-bad.jpg";
    private static final String OVERVIEW_BREAKING_BAD = "Um professor de quimica diagnosticado com cancer passa a produzir metanfetamina.";
    private static final Long ID_MATRIX = 1L;
    private static final String NOME_PRODUCAO_MATRIX = "Matrix";
    private static final String URL_IMAGEM_MATRIX = "https://image.tmdb.org/t/p/w400/matrix.jpg";
    private static final String OVERVIEW_MATRIX = "Um hacker descobre que a realidade e uma simulacao.";

    public static BuscaInicialResponse getBuscaInicialResponse() {
        return BuscaInicialResponse.builder()
                .pessoas(of(getKeanuReevesBuscaInicialPessoaResponse()))
                .series(of(getBreakingBadBuscaInicialProducaoResponse()))
                .filmes(of(getMatrixBuscaInicialProducaoResponse()))
                .build();
    }

    public static BuscaInicialPessoaResponse getKeanuReevesBuscaInicialPessoaResponse() {
        return BuscaInicialPessoaResponse.builder()
                .id(ID_KEANU_REEVES)
                .nome(NOME_KEANU_REEVES)
                .urlImagem(URL_IMAGEM_KEANU_REEVES)
                .build();
    }

    public static BuscaInicialProducaoResponse getBreakingBadBuscaInicialProducaoResponse() {
        return BuscaInicialProducaoResponse.builder()
                .id(ID_BREAKING_BAD)
                .nomeProducao(NOME_PRODUCAO_BREAKING_BAD)
                .urlImagem(URL_IMAGEM_BREAKING_BAD)
                .overview(OVERVIEW_BREAKING_BAD)
                .build();
    }

    public static BuscaInicialProducaoResponse getMatrixBuscaInicialProducaoResponse() {
        return BuscaInicialProducaoResponse.builder()
                .id(ID_MATRIX)
                .nomeProducao(NOME_PRODUCAO_MATRIX)
                .urlImagem(URL_IMAGEM_MATRIX)
                .overview(OVERVIEW_MATRIX)
                .build();
    }
}
