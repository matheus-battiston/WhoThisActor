package com.MovieParticipations.MovieParticipations.factories.domain;

import com.MovieParticipations.MovieParticipations.domain.FavoritaSerie;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class FavoritaSerieFactory {

    public static FavoritaSerie getFavoritaSerieEntity(Usuario usuario, Serie serie) {
        return FavoritaSerie.builder()
                .usuario(usuario)
                .serie(serie)
                .build();
    }

    public static FavoritaSerie getFavoritaSerieEntityComId(Usuario usuario, Serie serie, Long id) {
        FavoritaSerie favoritaSerie = getFavoritaSerieEntity(usuario, serie);
        favoritaSerie.setId(id);
        return favoritaSerie;
    }
}
