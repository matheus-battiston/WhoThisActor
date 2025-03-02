package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.SasResponse;

public class SASTokenMapper {
    public static SasResponse toResponse(String token){
        return  SasResponse.builder()
                .SASToken(token)
                .build();
    }
}
