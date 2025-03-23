package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.request.ClassificarApiExternaRequest;
import com.google.gson.Gson;

public class ClassificacaoApiExternaMapper {
    public static String toResponse(String url){
        Gson gson = new Gson();
        ClassificarApiExternaRequest requestObject = ClassificarApiExternaRequest.builder()
                .image_url(url)
                .build();

        return gson.toJson(requestObject);
    }
}
