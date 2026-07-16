package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;

import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.normalizar;

public class ProducaoMapper {
    public static ProducaoResponse toResponse(Serie serie) {
        return ProducaoResponse.builder()
                .nome(serie.getTitulo())
                .imagem(normalizar(serie.getImagem()))
                .genero(serie.getGenero())
                .overview(serie.getOverview())
                .ano(serie.getAnoPrimeiraTemporada())
                .id(serie.getId())
                .build();
    }

    public static ProducaoResponse toResponse(Filme filme) {
        return ProducaoResponse.builder()
                .nome(filme.getTitulo())
                .imagem(normalizar(filme.getImagem()))
                .genero(filme.getGenero())
                .overview(filme.getOverview())
                .ano(obterAnoLancamento(filme))
                .id(filme.getId())
                .build();
    }

    private static Integer obterAnoLancamento(Filme filme) {
        if (filme.getDataLancamento() == null) {
            return null;
        }

        return filme.getDataLancamento().getYear();
    }
}
