package com.MovieParticipations.MovieParticipations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AtorTMDBMovieDto implements AtorTMDBDto {

    @JsonProperty("name")
    String nome;
    @JsonProperty("profile_path")
    String fotoDePerfil;
    @JsonProperty("known_for_department")
    String conhecidoPor;
    @JsonProperty("popularity")
    Double popularidade;
    Long id;
    @JsonProperty("order")
    Long ordemDeCredito;
    @JsonProperty("character")
    String personagem;

}

