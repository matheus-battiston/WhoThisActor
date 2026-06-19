package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.security.domain.Funcao;
import com.MovieParticipations.MovieParticipations.security.domain.Permissao;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class PermissaoFactory {

    public static Permissao getPermissaoEntity(Usuario usuario) {
        Funcao funcao = Funcao.USUARIO;

        Permissao permissao = new Permissao();
        permissao.setFuncao(funcao);
        permissao.setUsuario(usuario);
        return permissao;
    }

    public static Permissao getPermissaoEntityComId(Usuario usuario, Long id) {
        Permissao permissao = getPermissaoEntity(usuario);
        permissao.setId(id);
        return permissao;
    }
}
