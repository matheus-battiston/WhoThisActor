package com.MovieParticipations.MovieParticipations.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CountryProviderDto {
    private String link;
    private List<ProviderDto> flatrate;
    private List<ProviderDto> buy;
    private List<ProviderDto> rent;
}
