package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.controller.response.ProducoesFavoritasResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;

import java.util.List;

public class ListaFavoritosMapper {
    public static ProducoesFavoritasResponse toResponse(List<Serie> seriesFavoritas, List<Filme> filmesFavoritos) {

        List<ProducaoResponse> series = seriesFavoritas
                .stream()
                .map(ProducaoMapper::toResponse)
                .toList();

        List<ProducaoResponse> filmes = filmesFavoritos
                .stream()
                .map(ProducaoMapper::toResponse)
                .toList();

        return ProducoesFavoritasResponse.builder()
                .series(series)
                .filmes(filmes)
                .build();
    }
}
