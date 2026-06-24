package com.MovieParticipations.MovieParticipations.factories.domain;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.FilmeAtor;

public class FilmeAtorFactory {

    public static FilmeAtor getFilmeAtorEntity(Filme filme, Ator ator, String personagem) {
        Long creditOrder = 1L;

        return FilmeAtor.builder()
                .filme(filme)
                .ator(ator)
                .personagem(personagem)
                .creditOrder(creditOrder)
                .build();
    }

    public static FilmeAtor getFilmeAtorEntity(Filme filme, Ator ator, String personagem, Long creditOrder) {
        return FilmeAtor.builder()
                .filme(filme)
                .ator(ator)
                .personagem(personagem)
                .creditOrder(creditOrder)
                .build();
    }

}
