package com.MovieParticipations.MovieParticipations.dto;


import lombok.*;

import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuscarProvidersDto {
    private Map<String, CountryProviderDto> results;
}
