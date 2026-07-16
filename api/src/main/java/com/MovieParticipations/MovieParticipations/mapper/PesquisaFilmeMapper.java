package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Filme;
import com.MovieParticipations.MovieParticipations.domain.TipoMidia;

public class PesquisaFilmeMapper {
    public static PesquisaProducaoInfoResponse toResponse(Filme novoFilme) {
        return PesquisaProducaoInfoResponse.builder()
                .nome(novoFilme.getTitulo())
                .imagem(novoFilme.getImagem())
                .dataLancamento(novoFilme.getDataLancamento())
                .overview(novoFilme.getOverview())
                .genero(novoFilme.getGenero())
                .tipoMidia(TipoMidia.MOVIE)
                .id(novoFilme.getId())
                .popularidade(novoFilme.getPopularidade())
                .build();
    }
}
