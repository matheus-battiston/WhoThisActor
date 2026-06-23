package com.MovieParticipations.MovieParticipations.factories.domain;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.SerieAtor;

public class SerieAtorFactory {

    public static SerieAtor getSerieAtorEntity(Serie serie, Ator ator, String personagem) {
        int numeroEpisodios = 1;

        return SerieAtor.builder()
                .serie(serie)
                .ator(ator)
                .personagem(personagem)
                .numeroEpisodios(numeroEpisodios)
                .build();
    }

    public static SerieAtor getSerieAtorEntity(Serie serie, Ator ator, String personagem, int numeroEpisodios) {
        return SerieAtor.builder()
                .serie(serie)
                .ator(ator)
                .personagem(personagem)
                .numeroEpisodios(numeroEpisodios)
                .build();
    }

}
