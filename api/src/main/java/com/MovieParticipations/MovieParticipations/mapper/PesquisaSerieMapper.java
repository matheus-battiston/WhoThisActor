package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.PesquisaProducaoInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.TV;
import static com.MovieParticipations.MovieParticipations.util.TmdbImagemUrl.normalizar;

public class PesquisaSerieMapper {

    public static PesquisaProducaoInfoResponse toResponse(Serie serie) {
        return PesquisaProducaoInfoResponse.builder()
                .nome(serie.getTitulo())
                .imagem(normalizar(serie.getImagem()))
                .overview(serie.getOverview())
                .genero(serie.getGenero())
                .anoPrimeiraTemporada(serie.getAnoPrimeiraTemporada())
                .anoUltimaTemporada(serie.getAnoUltimaTemporada())
                .tipoMidia(TV)
                .id(serie.getId())
                .popularidade(serie.getPopularidade())
                .build();
    }
}
