package com.MovieParticipations.MovieParticipations.security.mapper;

import com.MovieParticipations.MovieParticipations.security.controller.response.UsuarioHelloResponse;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class UsuarioHelloMapper {

    public static UsuarioHelloResponse toResponse(Usuario usuario, boolean primeiroLogin) {
        return UsuarioHelloResponse.builder()
                .id(usuario.getId()).
                nome(usuario.getNome()).
                email(usuario.getEmail()).
                primeiroLogin(primeiroLogin).
                build();
    }
}
