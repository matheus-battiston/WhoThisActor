package com.MovieParticipations.MovieParticipations.factories;

import com.matheus.libauth.security.dto.UsuarioAutenticado;

public class UsuarioAutenticadoFactory {

    public static UsuarioAutenticado get() {
        String nome = "Matheus";
        String email = "matheus@email.com";
        Long id = 7L;

        return new UsuarioAutenticado(nome, email, id);
    }
}
