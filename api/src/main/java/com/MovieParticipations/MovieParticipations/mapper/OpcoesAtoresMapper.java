package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.OpcoesAtoresParecidosResponse;
import com.MovieParticipations.MovieParticipations.dto.ClassificacaoResponseDTO;

public class OpcoesAtoresMapper {
    public static OpcoesAtoresParecidosResponse toResponse(ClassificacaoResponseDTO objeto){
        return OpcoesAtoresParecidosResponse.builder()
                .identidade(objeto.getIdentidade())
                .distanciaMedia(objeto.getDistanciaMedia())
                .build();
    }
}
