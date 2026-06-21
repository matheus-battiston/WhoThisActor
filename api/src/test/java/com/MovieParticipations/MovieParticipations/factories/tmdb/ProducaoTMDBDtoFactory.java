package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;

public class ProducaoTMDBDtoFactory {
    private static final Long ID_MATRIX = 123L;
    private static final String TITULO_MATRIX = "Matrix";
    private static final String TIPO_MIDIA_MOVIE = "movie";
    private static final Double POPULARIDADE_MATRIX = 10.0;
    private static final String IMAGEM_POSTER_MATRIX = "/matrix.jpg";
    private static final String PERSONAGEM_NEO = "Neo";
    private static final Long ORDEM_MATRIX = 1L;
    private static final Long ID_BREAKING_BAD = 456L;
    private static final String NOME_BREAKING_BAD = "Breaking Bad";
    private static final String TIPO_MIDIA_TV = "tv";
    private static final Double POPULARIDADE_BREAKING_BAD = 9.5;
    private static final String IMAGEM_POSTER_BREAKING_BAD = "/breaking-bad.jpg";
    private static final String PERSONAGEM_WALTER_WHITE = "Walter White";
    private static final Integer NUMERO_EPISODIOS_BREAKING_BAD = 62;
    private static final Long ORDEM_BREAKING_BAD = 1L;
    private static final Long ID_PRODUCAO_DESCONHECIDA = 789L;
    private static final String TITULO_PRODUCAO_DESCONHECIDA = "Producao Desconhecida";
    private static final String TIPO_MIDIA_DESCONHECIDO = "person";
    private static final Double POPULARIDADE_PRODUCAO_DESCONHECIDA = 5.0;
    private static final String IMAGEM_POSTER_PRODUCAO_DESCONHECIDA = "/producao-desconhecida.jpg";
    private static final String PERSONAGEM_PRODUCAO_DESCONHECIDA = "Personagem";
    private static final Long ORDEM_PRODUCAO_DESCONHECIDA = 3L;

    public static ProducaoTMDBDto getMatrixProducaoTMDBDto() {

        return ProducaoTMDBDto.builder()
                .id(ID_MATRIX)
                .titulo(TITULO_MATRIX)
                .tipoMidia(TIPO_MIDIA_MOVIE)
                .popularidade(POPULARIDADE_MATRIX)
                .imagemPoster(IMAGEM_POSTER_MATRIX)
                .personagem(PERSONAGEM_NEO)
                .ordem(ORDEM_MATRIX)
                .build();
    }

    public static ProducaoTMDBDto getBreakingBadProducaoTMDBDto() {

        return ProducaoTMDBDto.builder()
                .id(ID_BREAKING_BAD)
                .nome(NOME_BREAKING_BAD)
                .tipoMidia(TIPO_MIDIA_TV)
                .popularidade(POPULARIDADE_BREAKING_BAD)
                .imagemPoster(IMAGEM_POSTER_BREAKING_BAD)
                .personagem(PERSONAGEM_WALTER_WHITE)
                .numeroEpisodios(NUMERO_EPISODIOS_BREAKING_BAD)
                .ordem(ORDEM_BREAKING_BAD)
                .build();
    }

    public static ProducaoTMDBDto getProducaoComTipoMidiaDesconhecido() {

        return ProducaoTMDBDto.builder()
                .id(ID_PRODUCAO_DESCONHECIDA)
                .titulo(TITULO_PRODUCAO_DESCONHECIDA)
                .tipoMidia(TIPO_MIDIA_DESCONHECIDO)
                .popularidade(POPULARIDADE_PRODUCAO_DESCONHECIDA)
                .imagemPoster(IMAGEM_POSTER_PRODUCAO_DESCONHECIDA)
                .personagem(PERSONAGEM_PRODUCAO_DESCONHECIDA)
                .ordem(ORDEM_PRODUCAO_DESCONHECIDA)
                .build();
    }

    public static ProducaoTMDBDto getProducaoSemTituloENome() {
        return ProducaoTMDBDto.builder()
                .id(ID_PRODUCAO_DESCONHECIDA)
                .tipoMidia(TIPO_MIDIA_DESCONHECIDO)
                .popularidade(POPULARIDADE_PRODUCAO_DESCONHECIDA)
                .imagemPoster(IMAGEM_POSTER_PRODUCAO_DESCONHECIDA)
                .personagem(PERSONAGEM_PRODUCAO_DESCONHECIDA)
                .ordem(ORDEM_PRODUCAO_DESCONHECIDA)
                .build();
    }
}
