package com.MovieParticipations.MovieParticipations.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SerieTMDBDto {
    Long id;
    @JsonProperty("name")
    String nome;
    @JsonProperty("poster_path")
    String imagemPoster;
    @JsonProperty("popularity")
    Double popularidade;
    @JsonProperty("original_name")
    String nomeOriginal;
}
