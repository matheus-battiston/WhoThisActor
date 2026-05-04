package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.domain.FavoritaFilme;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class FavoritaFilmeMapper {
    public static FavoritaFilme toEntity(Filme filme, Usuario usuario) {
        return FavoritaFilme.builder()
                .filme(filme)
                .usuario(usuario)
                .build();
    }
}
