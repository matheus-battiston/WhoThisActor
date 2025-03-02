package com.MovieParticipations.MovieParticipations.security.service;


import com.MovieParticipations.MovieParticipations.security.controller.request.UsuarioRequest;
import com.MovieParticipations.MovieParticipations.security.domain.Permissao;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.repository.UsuarioRepository;
import com.MovieParticipations.MovieParticipations.service.UsuarioNovoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.MovieParticipations.MovieParticipations.security.domain.Funcao.USUARIO;
import static com.MovieParticipations.MovieParticipations.security.mapper.UsuarioMapper.toEntity;


@Service
public class IncluirUsuarioOAuthService {

    private static final String NOME_OAUTH = "name";
    private static final String EMAIL_OAUTH = "email";

    @Autowired
    private UsuarioNovoService usuarioNovoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void incluirViaOauth(OAuth2User oAuth2User){
        if (usuarioNovoService.porEmail(oAuth2User.getAttribute(EMAIL_OAUTH)))
            return;

        UsuarioRequest novoUsuario = new UsuarioRequest();
        novoUsuario.setNome(oAuth2User.getAttribute(NOME_OAUTH));
        novoUsuario.setEmail(oAuth2User.getAttribute(EMAIL_OAUTH));

        Usuario usuario = toEntity(novoUsuario);
        usuario.setAtivo(true);
        usuario.adicionarPermissao(getPermissaoPadrao());
        usuario.setProvider(true);
        usuarioRepository.save(usuario);

    }

    private Permissao getPermissaoPadrao() {
        Permissao permissao = new Permissao();
        permissao.setFuncao(USUARIO);
        return permissao;
    }
}
