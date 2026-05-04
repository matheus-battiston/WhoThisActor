package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.repository.FavoritaFilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.FavoritaSerieRepository;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoritosClassificacaoService {

    private final FavoritaSerieRepository favoritaSerieRepository;
    private final FavoritaFilmeRepository favoritaFilmeRepository;

    public List<Long> buscarIdsSeriesFavoritas(UsuarioAutenticado usuarioAutenticado) {
        return favoritaSerieRepository.findSerieIdByAuthUserId(usuarioAutenticado.getId());
    }

    public List<Long> buscarIdsFilmesFavoritos(UsuarioAutenticado usuarioAutenticado) {
        return favoritaFilmeRepository.findFilmesIdByAuthUserId(usuarioAutenticado.getId());
    }
}
