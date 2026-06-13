package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.FavoritaSerie;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class FavoritaSerieFactory {

    public static FavoritaSerie get(Usuario usuario, Serie serie) {
        return FavoritaSerie.builder()
                .usuario(usuario)
                .serie(serie)
                .build();
    }

    public static FavoritaSerie getComId(Usuario usuario, Serie serie, Long id) {
        FavoritaSerie favoritaSerie = get(usuario, serie);
        favoritaSerie.setId(id);
        return favoritaSerie;
    }
}
