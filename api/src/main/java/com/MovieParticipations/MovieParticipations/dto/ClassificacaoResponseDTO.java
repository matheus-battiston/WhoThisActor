package com.MovieParticipations.MovieParticipations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassificacaoResponseDTO {

    @JsonProperty("label")
    private String identidade;

    @JsonProperty("pontuacao")
    private Double distanciaMedia;
}