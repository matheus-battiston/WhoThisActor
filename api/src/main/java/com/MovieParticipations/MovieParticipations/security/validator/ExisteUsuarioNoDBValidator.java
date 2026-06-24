package com.MovieParticipations.MovieParticipations.security.validator;

import com.MovieParticipations.MovieParticipations.security.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExisteUsuarioNoDBValidator {
    private final UsuarioRepository usuarioRepository;

    public boolean ExisteUsuarioComEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
