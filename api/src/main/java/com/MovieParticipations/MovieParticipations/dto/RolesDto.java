package com.MovieParticipations.MovieParticipations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RolesDto {

    @JsonProperty("character")
    private String personagem;
    @JsonProperty("episode_count")
    private int numeroEpisodios;
}