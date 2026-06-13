package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.FavoritaAtor;
import com.MovieParticipations.MovieParticipations.domain.FavoritaSerie;
import com.MovieParticipations.MovieParticipations.security.domain.Permissao;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioFactory {

    public static Usuario get() {
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

    public static Usuario get(Long id) {
        Usuario usuario = get();
        usuario.setId(id);
        return usuario;
    }

    public static Usuario getInativo() {
        Long authUserId = 78L;
        String nome = "Usuario Inativo";
        String email = "inativo@email.com";
        boolean ativo = false;
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
}
