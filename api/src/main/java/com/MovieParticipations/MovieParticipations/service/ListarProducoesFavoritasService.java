package com.MovieParticipations.MovieParticipations.service;


import com.MovieParticipations.MovieParticipations.controller.response.ProducoesFavoritasResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.mapper.ListaFavoritosMapper;
import com.MovieParticipations.MovieParticipations.repository.FavoritaFilmeRepository;
import com.MovieParticipations.MovieParticipations.repository.FavoritaSerieRepository;
import com.matheus.libauth.security.dto.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListarProducoesFavoritasService {
    private final FavoritaSerieRepository favoritaSerieRepository;
    private final FavoritaFilmeRepository favoritaFilmeRepository;

    public ProducoesFavoritasResponse listaDeFavoritos(UsuarioAutenticado usuarioAutenticado) {
        List<Serie> seriesFavoritas = favoritaSerieRepository.findSeriesByAuthUserId(usuarioAutenticado.getId());
        List<Filme> filmesFavoritos = favoritaFilmeRepository.findFilmesByAuthUserId(usuarioAutenticado.getId());

        return ListaFavoritosMapper.toResponse(seriesFavoritas, filmesFavoritos);
    }
}
