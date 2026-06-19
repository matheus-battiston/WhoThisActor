package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class FavoritaAtorFactory {

    public static FavoritaAtor getFavoritaAtorEntity(Usuario usuario, Ator ator) {
        return FavoritaAtor.builder()
                .usuario(usuario)
                .ator(ator)
                .build();
    }

    public static FavoritaAtor getFavoritaAtorEntityComId(Usuario usuario, Ator ator, Long id) {
        FavoritaAtor favoritaAtor = getFavoritaAtorEntity(usuario, ator);
        favoritaAtor.setId(id);
        return favoritaAtor;
    }
}
