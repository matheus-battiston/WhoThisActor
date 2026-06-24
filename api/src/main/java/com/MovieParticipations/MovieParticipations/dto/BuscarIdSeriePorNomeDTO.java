package com.MovieParticipations.MovieParticipations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuscarIdSeriePorNomeDTO {
    @JsonProperty("results")
    List<SerieTMDBDto> resultados;
}
