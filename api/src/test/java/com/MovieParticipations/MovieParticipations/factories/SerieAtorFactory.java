package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.domain.SerieAtor;

public class SerieAtorFactory {

    public static SerieAtor get(Serie serie, Ator ator, String personagem) {
        int numeroEpisodios = 1;

        return SerieAtor.builder()
                .serie(serie)
                .ator(ator)
                .personagem(personagem)
                .numeroEpisodios(numeroEpisodios)
                .build();
    }

    public static SerieAtor get(Serie serie, Ator ator, String personagem, int numeroEpisodios) {
        return SerieAtor.builder()
                .serie(serie)
                .ator(ator)
                .personagem(personagem)
                .numeroEpisodios(numeroEpisodios)
                .build();
    }

    public static SerieAtor getComId(Serie serie, Ator ator, String personagem, Long id) {
        SerieAtor serieAtor = get(serie, ator, personagem);
        serieAtor.setId(id);
        return serieAtor;
    }

    public static SerieAtor getWalterWhite() {
        Serie serie = SerieFactory.getBreakingBad();
        Ator ator = AtorFactory.getBryanCranston();
        String personagem = "Walter White";
        int numeroEpisodios = 62;

        return get(serie, ator, personagem, numeroEpisodios);
    }
}
