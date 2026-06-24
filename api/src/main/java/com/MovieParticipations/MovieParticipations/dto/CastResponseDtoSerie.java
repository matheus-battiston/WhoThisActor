package com.MovieParticipations.MovieParticipations.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CastResponseDtoSerie {
    @JsonProperty("cast")
    List<AtorTMDBSerieDto> elenco;
}
