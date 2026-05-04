package com.MovieParticipations.MovieParticipations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProviderDto {
    @JsonProperty("logo_path")
    private String imagemLogo;

    @JsonProperty("provider_id")
    private int idProvider;

    @JsonProperty("provider_name")
    private String nomeProvider;

    @JsonProperty("display_priority")
    private int prioridade;
}
