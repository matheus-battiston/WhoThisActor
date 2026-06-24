package com.MovieParticipations.MovieParticipations.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProducaoTMDBDto {
    Long id;
    @JsonProperty("title")
    String titulo;
    @JsonProperty("name")
    String nome;
    @JsonProperty("media_type")
    String tipoMidia;
    @JsonProperty("popularity")
    Double popularidade;
    @JsonProperty("poster_path")
    String imagemPoster;
    @JsonProperty("character")
    String personagem;
    @JsonProperty("episode_count")
    Integer numeroEpisodios;
    @JsonProperty("order")
    Long ordem;

}
