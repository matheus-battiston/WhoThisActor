package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioNovoService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean porEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

}
