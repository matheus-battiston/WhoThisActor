package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.security.domain.Funcao;
import com.MovieParticipations.MovieParticipations.security.domain.Permissao;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;

public class PermissaoFactory {

    public static Permissao get(Usuario usuario) {
        Funcao funcao = Funcao.USUARIO;

        Permissao permissao = new Permissao();
        permissao.setFuncao(funcao);
        permissao.setUsuario(usuario);
        return permissao;
    }

    public static Permissao getComId(Usuario usuario, Long id) {
        Permissao permissao = get(usuario);
        permissao.setId(id);
        return permissao;
    }
}
