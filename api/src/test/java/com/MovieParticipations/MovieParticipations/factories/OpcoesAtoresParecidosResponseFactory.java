package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;

public class OpcoesAtoresParecidosResponseFactory {

    public static OpcoesAtoresParecidosResponse getKeanuReeves() {
        return OpcoesAtoresParecidosResponse.builder()
                .identidade("Keanu Reeves")
                .distanciaMedia(0.15)
                .build();
    }
}
