package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.InfoAtorResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.normalizar;

public class FavoritaAtorMapper {
    public static FavoritaAtor toEntity(Ator ator, Usuario usuario) {
        return FavoritaAtor.builder()
                .ator(ator)
                .usuario(usuario)
                .build();
    }

    public static InfoAtorResponse toResponse (Ator ator){
        return InfoAtorResponse.builder()
                .nome(ator.getNome())
                .id(ator.getId())
                .urlImagem(normalizar(ator.getImagem()))
                .build();
    }
}
