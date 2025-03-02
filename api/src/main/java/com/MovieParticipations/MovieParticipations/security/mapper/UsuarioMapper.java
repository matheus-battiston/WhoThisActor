package com.MovieParticipations.MovieParticipations.security.mapper;

import com.MovieParticipations.MovieParticipations.security.controller.request.UsuarioRequest;
import com.MovieParticipations.MovieParticipations.security.controller.response.UsuarioResponse;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequest request) {
        Usuario entity = new Usuario();
        entity.setNome(request.getNome());
        entity.setEmail(request.getEmail());

        return entity;
    }

    public static UsuarioResponse toResponse(Usuario entity) {
        return UsuarioResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .build();
    }
}
