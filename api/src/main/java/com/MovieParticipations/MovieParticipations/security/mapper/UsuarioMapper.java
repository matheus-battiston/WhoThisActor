package com.MovieParticipations.MovieParticipations.security.mapper;

import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.matheus.libauth.security.dto.UsuarioAutenticado;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioAutenticado usuario) {
        return Usuario.builder()
                .ativo(true)
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .authUserId(usuario.getId())
                .build();
    }

}
