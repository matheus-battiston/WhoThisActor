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
    private String logoPath;

    @JsonProperty("provider_id")
    private int providerId;

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("display_priority")
    private int displayPriority;
}
