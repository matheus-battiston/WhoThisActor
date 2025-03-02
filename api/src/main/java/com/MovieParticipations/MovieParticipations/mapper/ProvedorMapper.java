package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.ProvedoresResponse;
import com.MovieParticipations.MovieParticipations.domain.Provedores;

public class ProvedorMapper {

    public static ProvedoresResponse toResponse(Provedores provedor){
        return ProvedoresResponse.builder()
                .provedor(provedor)
                .build();
    }
}
