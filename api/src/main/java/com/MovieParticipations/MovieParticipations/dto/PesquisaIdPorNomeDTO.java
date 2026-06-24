package com.MovieParticipations.MovieParticipations.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PesquisaIdPorNomeDTO {
    @JsonProperty("results")
    List<AtorTMDBDtoPesquisaId> resultados;
}
