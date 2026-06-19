package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;

public class OpcoesAtoresParecidosResponseFactory {
    private static final String IDENTIDADE_KEANU_REEVES = "Keanu Reeves";
    private static final Double DISTANCIA_MEDIA_KEANU_REEVES = 0.15;

    public static OpcoesAtoresParecidosResponse getKeanuReevesOpcoesAtoresParecidosResponse() {

        return OpcoesAtoresParecidosResponse.builder()
                .identidade(IDENTIDADE_KEANU_REEVES)
                .distanciaMedia(DISTANCIA_MEDIA_KEANU_REEVES)
                .build();
    }
}
