package com.MovieParticipations.MovieParticipations.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AtorTMDBSerieDto implements AtorTMDBDto{
    @JsonProperty("name")
    String nome;
    @JsonProperty("profile_path")
    String fotoDePerfil;
    @JsonProperty("known_for_department")
    String conhecidoPor;
    @JsonProperty("roles")
    List<RolesDto> papeis;
    @JsonProperty("popularity")
    Double popularidade;
    Long id;
}
