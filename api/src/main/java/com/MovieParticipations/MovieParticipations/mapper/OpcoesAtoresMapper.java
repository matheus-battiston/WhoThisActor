package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.google.gson.JsonElement;

public class OpcoesAtoresMapper {
    private static final String PARAMETRO_IDENTITY = "identity";
    private static final String PARAMETRO_DISTANCIA = "average_distance";
    public static OpcoesAtoresParecidosResponse toResponse(JsonElement objeto){
        return OpcoesAtoresParecidosResponse.builder()
                .identity(objeto.getAsJsonObject().get(PARAMETRO_IDENTITY).getAsString())
                .averageDistance(objeto.getAsJsonObject().get(PARAMETRO_DISTANCIA).getAsDouble())
                .build();
    }
}
