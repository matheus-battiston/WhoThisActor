package com.MovieParticipations.MovieParticipations.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AtorTMDBDtoPesquisaId implements AtorTMDBDto{
    Long id;
    @JsonProperty("name")
    String nome;
    @JsonProperty("known_for_department")
    String conhecidoPor;
    @JsonProperty("popularity")
    Double popularidade;
    @JsonProperty("profile_path")
    String fotoDePerfil;
}
