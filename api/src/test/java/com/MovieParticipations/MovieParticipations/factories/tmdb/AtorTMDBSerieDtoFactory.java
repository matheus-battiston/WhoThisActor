package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.dto.RolesDto;

import java.util.List;

public class AtorTMDBSerieDtoFactory {
    private static final Long ID_TMDB_BRYAN_CRANSTON = 17419L;
    private static final String NOME_BRYAN_CRANSTON = "Bryan Cranston";
    private static final String FOTO_BRYAN_CRANSTON = "/bryan-cranston.jpg";
    private static final Double POPULARIDADE_BRYAN_CRANSTON = 12.0;
    private static final String PERSONAGEM_WALTER_WHITE = "Walter White";
    private static final String PERSONAGEM_WALTER_WHITE_HEISENBERG = "Walter White / Heisenberg";
    private static final int EPISODIOS_WALTER_WHITE = 62;

    private static final Long ID_TMDB_AARON_PAUL = 84497L;
    private static final String NOME_AARON_PAUL = "Aaron Paul";
    private static final String FOTO_AARON_PAUL = "/aaron-paul.jpg";
    private static final Double POPULARIDADE_AARON_PAUL = 9.0;
    private static final String PERSONAGEM_JESSE_PINKMAN = "Jesse Pinkman";
    private static final int EPISODIOS_JESSE_PINKMAN = 62;

    private static final String CONHECIDO_POR_ACTING = "Acting";
    private static final String CONHECIDO_POR_WRITING = "Writing";

    public static AtorTMDBSerieDto getBryanCranstonAtorTMDBSerieDto() {
        return criarBryanCranston(PERSONAGEM_WALTER_WHITE);
    }

    public static AtorTMDBSerieDto getBryanCranstonComPersonagemMaiorAtorTMDBSerieDto() {
        return criarBryanCranston(PERSONAGEM_WALTER_WHITE_HEISENBERG);
    }

    public static AtorTMDBSerieDto getAaronPaulAtorTMDBSerieDto() {
        return criarAaronPaul(CONHECIDO_POR_ACTING);
    }

    public static AtorTMDBSerieDto getAaronPaulRoteiristaAtorTMDBSerieDto() {
        return criarAaronPaul(CONHECIDO_POR_WRITING);
    }

    private static AtorTMDBSerieDto criarAaronPaul(String conhecidoPor) {
        return AtorTMDBSerieDto.builder()
                .id(ID_TMDB_AARON_PAUL)
                .nome(NOME_AARON_PAUL)
                .conhecidoPor(conhecidoPor)
                .fotoDePerfil(FOTO_AARON_PAUL)
                .popularidade(POPULARIDADE_AARON_PAUL)
                .papeis(List.of(criarPapel(PERSONAGEM_JESSE_PINKMAN, EPISODIOS_JESSE_PINKMAN)))
                .build();
    }

    private static AtorTMDBSerieDto criarBryanCranston(String personagem) {
        return AtorTMDBSerieDto.builder()
                .id(ID_TMDB_BRYAN_CRANSTON)
                .nome(NOME_BRYAN_CRANSTON)
                .conhecidoPor(CONHECIDO_POR_ACTING)
                .fotoDePerfil(FOTO_BRYAN_CRANSTON)
                .popularidade(POPULARIDADE_BRYAN_CRANSTON)
                .papeis(List.of(criarPapel(personagem, EPISODIOS_WALTER_WHITE)))
                .build();
    }

    private static RolesDto criarPapel(String personagem, int episodios) {
        return RolesDto.builder()
                .personagem(personagem)
                .numeroEpisodios(episodios)
                .build();
    }
}
