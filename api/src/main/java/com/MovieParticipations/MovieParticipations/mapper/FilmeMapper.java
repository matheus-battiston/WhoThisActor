package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProducaoResponse;
import com.MovieParticipations.MovieParticipations.domain.TipoProducao;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;
import com.google.gson.JsonObject;

import static com.MovieParticipations.MovieParticipations.domain.TipoProducao.*;

public class FilmeMapper {
    private static final String VALOR_FILME = "movie";

    public static ProducaoResponse toResponse(ProducaoTMDBDto producao){
        TipoProducao tipoProducao = definirTipoProducao(producao.getMedia_type());

        return ProducaoResponse.builder()
                .id(producao.getId().intValue())
                .nomePersonagem(producao.getCharacter())
                .posterLink(producao.getPoster_path())
                .tipoProducao(tipoProducao)
                .popularidade(Float.valueOf(producao.getPopularity()))
                .nomeProducao(getNomeProducao(producao, tipoProducao))
                .build();

    }

    private static String getNomeProducao(ProducaoTMDBDto producao, TipoProducao tipoProducao) {
        return tipoProducao.equals(TV) ? producao.getName() : producao.getTitle();
    }

    private static TipoProducao definirTipoProducao(String tipoProducao){
        if (tipoProducao.equals(VALOR_FILME)) {
            return FILME;
        }
        else return TV;
    }
}