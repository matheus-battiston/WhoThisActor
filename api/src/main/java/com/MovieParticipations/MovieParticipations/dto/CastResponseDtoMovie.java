package com.MovieParticipations.MovieParticipations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CastResponseDtoMovie {
    @JsonProperty("cast")
    private List<AtorTMDBMovieDto> elenco;

}