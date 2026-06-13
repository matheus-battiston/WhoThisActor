package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class FavoritaAtorFactory {

    public static FavoritaAtor get(Usuario usuario, Ator ator) {
        return FavoritaAtor.builder()
                .usuario(usuario)
                .ator(ator)
                .build();
    }

    public static FavoritaAtor getComId(Usuario usuario, Ator ator, Long id) {
        FavoritaAtor favoritaAtor = get(usuario, ator);
        favoritaAtor.setId(id);
        return favoritaAtor;
    }
}
