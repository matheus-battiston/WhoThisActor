package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialPessoaResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialProducaoResponse;
import com.MovieParticipations.MovieParticipations.controller.response.BuscaInicialResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl;

import java.util.List;

public final class BuscaInicialMapper {

    private BuscaInicialMapper() {
    }

    public static BuscaInicialPessoaResponse toPessoaResponse(Ator ator) {
        return BuscaInicialPessoaResponse.builder()
                .id(ator.getId())
                .nome(ator.getNome())
                .urlImagem(TmdbImagemUrl.normalizar(ator.getImagem()))
                .build();
    }

    public static BuscaInicialProducaoResponse toProducaoResponse(Filme filme) {
        return BuscaInicialProducaoResponse.builder()
                .id(filme.getId())
                .nomeProducao(filme.getTitulo())
                .urlImagem(TmdbImagemUrl.normalizar(filme.getImagem()))
                .overview(filme.getOverview())
                .build();
    }

    public static BuscaInicialProducaoResponse toProducaoResponse(Serie serie) {
        return BuscaInicialProducaoResponse.builder()
                .id(serie.getId())
                .nomeProducao(serie.getTitulo())
                .urlImagem(TmdbImagemUrl.normalizar(serie.getImagem()))
                .overview(serie.getOverview())
                .build();
    }

    public static BuscaInicialResponse toResponse(
            List<BuscaInicialPessoaResponse> pessoas,
            List<BuscaInicialProducaoResponse> series,
            List<BuscaInicialProducaoResponse> filmes
    ) {
        return BuscaInicialResponse.builder()
                .pessoas(pessoas)
                .series(series)
                .filmes(filmes)
                .build();
    }
}
