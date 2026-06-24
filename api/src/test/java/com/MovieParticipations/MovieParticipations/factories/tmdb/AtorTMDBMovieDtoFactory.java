package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;

public class AtorTMDBMovieDtoFactory {
    private static final Long ID_TMDB_KEANU_REEVES = 6384L;
    private static final String NOME_KEANU_REEVES = "Keanu Reeves";
    private static final String CONHECIDO_POR_ACTING = "Acting";
    private static final String CONHECIDO_POR_DIRECTING = "Directing";
    private static final String FOTO_KEANU_REEVES = "/keanu.jpg";
    private static final Double POPULARIDADE_KEANU_REEVES = 10.0;
    private static final Long ORDEM_CREDITO_NEO = 1L;
    private static final String PERSONAGEM_NEO = "Neo";

    public static AtorTMDBMovieDto getKeanuReevesAtorTMDBMovieDto() {
        return criarKeanuReeves(CONHECIDO_POR_ACTING);
    }

    public static AtorTMDBMovieDto getKeanuReevesDiretorAtorTMDBMovieDto() {
        return criarKeanuReeves(CONHECIDO_POR_DIRECTING);
    }

    private static AtorTMDBMovieDto criarKeanuReeves(String conhecidoPor) {
        return AtorTMDBMovieDto.builder()
                .id(ID_TMDB_KEANU_REEVES)
                .nome(NOME_KEANU_REEVES)
                .conhecidoPor(conhecidoPor)
                .fotoDePerfil(FOTO_KEANU_REEVES)
                .popularidade(POPULARIDADE_KEANU_REEVES)
                .ordemDeCredito(ORDEM_CREDITO_NEO)
                .personagem(PERSONAGEM_NEO)
                .build();
    }
}
