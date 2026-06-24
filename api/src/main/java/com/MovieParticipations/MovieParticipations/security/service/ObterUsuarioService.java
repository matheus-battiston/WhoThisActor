package com.MovieParticipations.MovieParticipations.security.service;

import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class ObterUsuarioService {
    private static final String USUARIO_NAO_EXISTE = "O usuario com essa ID nao existe";
    private final UsuarioRepository usuarioRepository;

    public Usuario obterComAuthId(Long idAuthUsuario){
        return usuarioRepository.findByAuthUserId(idAuthUsuario)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, USUARIO_NAO_EXISTE));
    }
}
