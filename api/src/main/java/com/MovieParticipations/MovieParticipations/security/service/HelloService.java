package com.MovieParticipations.MovieParticipations.security.service;

import com.MovieParticipations.MovieParticipations.security.controller.response.UsuarioHelloResponse;
import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.mapper.UsuarioHelloMapper;
import com.MovieParticipations.MovieParticipations.security.repository.UsuarioRepository;
import com.MovieParticipations.MovieParticipations.security.validator.ExisteUsuarioNoDBValidator;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HelloService {

    private static final String USUARIO_NAO_ENCONTRADO = "Usuario nao encontrado";

    private final ExisteUsuarioNoDBValidator existeUsuarioNoDBValidator;
    private final UsuarioNovoService usuarioNovoService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioHelloResponse hello(UsuarioAutenticado usuarioAutenticado) {
        boolean primeiroLogin = false;

        if (!existeUsuarioNoDBValidator.ExisteUsuarioComEmail(usuarioAutenticado.getEmail())){
            usuarioNovoService.addUsuario(usuarioAutenticado);
            primeiroLogin = true;
        }

        Usuario usuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail())
                .orElseThrow(() -> new RuntimeException(USUARIO_NAO_ENCONTRADO));

        return UsuarioHelloMapper.toResponse(usuario, primeiroLogin);
    }
}
