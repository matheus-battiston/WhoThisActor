package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.FavoritaFilme;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class FavoritaFilmeFactory {

    public static FavoritaFilme getFavoritaFilmeEntity(Usuario usuario, Filme filme) {
        return FavoritaFilme.builder()
                .usuario(usuario)
                .filme(filme)
                .build();
    }

    public static FavoritaFilme getFavoritaFilmeEntityComId(Usuario usuario, Filme filme, Long id) {
        FavoritaFilme favoritaFilme = getFavoritaFilmeEntity(usuario, filme);
        favoritaFilme.setId(id);
        return favoritaFilme;
    }
}
