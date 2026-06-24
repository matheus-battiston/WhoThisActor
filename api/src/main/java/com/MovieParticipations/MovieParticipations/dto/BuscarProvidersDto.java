package com.MovieParticipations.MovieParticipations.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuscarProvidersDto {
    @JsonProperty("results")
    private Map<String, CountryProviderDto> resultados;
}
