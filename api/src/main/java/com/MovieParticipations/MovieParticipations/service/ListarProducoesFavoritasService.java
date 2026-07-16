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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListarProducoesFavoritasService {
    private final FavoritaSerieRepository favoritaSerieRepository;
    private final FavoritaFilmeRepository favoritaFilmeRepository;
    private final AtualizarSerieInfoService atualizarSerieInfoService;
    private final AtualizarFilmeInfoService atualizarFilmeInfoService;

    @Transactional
    public ProducoesFavoritasResponse listaDeFavoritos(UsuarioAutenticado usuarioAutenticado) {
        List<Serie> seriesFavoritas = favoritaSerieRepository.findSeriesByAuthUserId(usuarioAutenticado.getId())
                .stream()
                .map(this::prepararSerie)
                .toList();
        List<Filme> filmesFavoritos = favoritaFilmeRepository.findFilmesByAuthUserId(usuarioAutenticado.getId())
                .stream()
                .map(this::prepararFilme)
                .toList();

        return ListaFavoritosMapper.toResponse(seriesFavoritas, filmesFavoritos);
    }

    private Serie prepararSerie(Serie serie) {
        if (!Boolean.TRUE.equals(serie.getInfoAtualizado())) atualizarSerieInfoService.atualizar(serie);
        return serie;
    }

    private Filme prepararFilme(Filme filme) {
        if (!Boolean.TRUE.equals(filme.getInfoAtualizado())) atualizarFilmeInfoService.atualizar(filme);
        return filme;
    }
}
