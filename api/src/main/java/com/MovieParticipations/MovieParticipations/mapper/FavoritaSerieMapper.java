package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.domain.FavoritaSerie;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class FavoritaSerieMapper {
    public static FavoritaSerie toEntity(Serie serie, Usuario usuario) {
        return FavoritaSerie.builder()
                .serie(serie)
                .usuario(usuario)
                .build();
    }

}
