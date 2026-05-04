package com.MovieParticipations.MovieParticipations.security.service;

import com.MovieParticipations.MovieParticipations.security.domain.Usuario;
import com.MovieParticipations.MovieParticipations.security.repository.UsuarioRepository;
import com.MovieParticipations.MovieParticipations.security.validator.ExisteUsuarioNoDBValidator;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.MovieParticipations.MovieParticipations.security.mapper.UsuarioMapper.toEntity;

@RequiredArgsConstructor
@Service
public class UsuarioNovoService {

    private final UsuarioRepository usuarioRepository;
    private final ExisteUsuarioNoDBValidator existeUsuarioNoDBValidator;

    @Transactional
    public void addUsuario(UsuarioAutenticado usuarioAutenticado) {
        Usuario novoUsuario = toEntity(usuarioAutenticado);
        if (!existeUsuarioNoDBValidator.ExisteUsuarioComEmail(novoUsuario.getEmail()))
            usuarioRepository.save(novoUsuario);
    }
}
