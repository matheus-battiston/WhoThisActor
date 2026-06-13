package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.FilmeAtor;

public class FilmeAtorFactory {

    public static FilmeAtor get(Filme filme, Ator ator, String personagem) {
        Long creditOrder = 1L;

        return FilmeAtor.builder()
                .filme(filme)
                .ator(ator)
                .personagem(personagem)
                .creditOrder(creditOrder)
                .build();
    }

    public static FilmeAtor get(Filme filme, Ator ator, String personagem, Long creditOrder) {
        return FilmeAtor.builder()
                .filme(filme)
                .ator(ator)
                .personagem(personagem)
                .creditOrder(creditOrder)
                .build();
    }

    public static FilmeAtor getComId(Filme filme, Ator ator, String personagem, Long id) {
        FilmeAtor filmeAtor = get(filme, ator, personagem);
        filmeAtor.setId(id);
        return filmeAtor;
    }

    public static FilmeAtor getNeo() {
        Filme filme = FilmeFactory.getMatrix();
        Ator ator = AtorFactory.getKeanuReeves();
        String personagem = "Neo";
        Long creditOrder = 1L;

        return get(filme, ator, personagem, creditOrder);
    }
}
