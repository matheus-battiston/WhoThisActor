package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.domain.Ator;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;

import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.normalizar;

public class AtorClassificadoMapper {

    public static OpcoesAtoresParecidosResponse toResponse(
            OpcoesAtoresParecidosResponse classificacao,
            Ator ator
    ) {
        return OpcoesAtoresParecidosResponse.builder()
                .identidade(classificacao.getIdentidade())
                .distanciaMedia(classificacao.getDistanciaMedia())
                .id(ator.getId())
                .imagem(normalizar(ator.getImagem()))
                .idTmdb(ator.getIdTmdb())
                .popularidade(ator.getPopularity())
                .build();
    }

    public static OpcoesAtoresParecidosResponse toResponse(
            OpcoesAtoresParecidosResponse classificacao,
            AtorTMDBDtoPesquisaId ator
    ) {
        return OpcoesAtoresParecidosResponse.builder()
                .identidade(classificacao.getIdentidade())
                .distanciaMedia(classificacao.getDistanciaMedia())
                .imagem(normalizar(ator.getFotoDePerfil()))
                .idTmdb(ator.getId())
                .popularidade(ator.getPopularidade())
                .build();
    }
}
