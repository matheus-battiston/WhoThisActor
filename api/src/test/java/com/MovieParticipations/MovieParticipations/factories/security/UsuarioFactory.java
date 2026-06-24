package com.MovieParticipations.MovieParticipations.factories.security;

import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import com.MovieParticipations.MovieParticipations.domain.FavoritaSerie;
import com.MovieParticipations.MovieParticipations.security.domain.Permissao;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioFactory {

    public static Usuario getUsuarioEntity() {
        Long authUserId = 77L;
        String nome = "Matheus";
        String email = "matheus@email.com";
        boolean ativo = true;
        List<FavoritaAtor> favoritos = new ArrayList<>();
        List<FavoritaSerie> seriesFavoritas = new ArrayList<>();
        List<Permissao> permissoes = new ArrayList<>();

        return Usuario.builder()
                .authUserId(authUserId)
                .nome(nome)
                .email(email)
                .ativo(ativo)
                .favoritos(favoritos)
                .seriesFavoritas(seriesFavoritas)
                .permissoes(permissoes)
                .build();
    }

    public static Usuario getUsuarioEntityComId(Long id) {
        Usuario usuario = getUsuarioEntity();
        usuario.setId(id);
        return usuario;
    }

}
