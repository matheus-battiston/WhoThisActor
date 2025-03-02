package com.MovieParticipations.MovieParticipations.security.service;


import com.MovieParticipations.MovieParticipations.security.controller.request.UsuarioRequest;
import com.MovieParticipations.MovieParticipations.security.controller.response.UsuarioResponse;
import com.MovieParticipations.MovieParticipations.security.domain.Permissao;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.MovieParticipations.MovieParticipations.security.domain.Funcao.USUARIO;
import static com.MovieParticipations.MovieParticipations.security.mapper.UsuarioMapper.toEntity;
import static com.MovieParticipations.MovieParticipations.security.mapper.UsuarioMapper.toResponse;

@Service
public class IncluirUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioResponse incluir(UsuarioRequest request) {

        Usuario usuario = toEntity(request);
        usuario.setSenha(getSenhaCriptografada(request.getSenha()));
        usuario.adicionarPermissao(getPermissaoPadrao());
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

    private String getSenhaCriptografada(String senhaAberta) {
        return passwordEncoder.encode(senhaAberta);
    }

    private Permissao getPermissaoPadrao() {
        Permissao permissao = new Permissao();
        permissao.setFuncao(USUARIO);
        return permissao;
    }
}
