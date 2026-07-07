package com.MovieParticipations.MovieParticipations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SerieDetalhesTMDBDto {
    private Long id;
    @JsonProperty("name")
    private String nome;
    @JsonProperty("original_name")
    private String nomeOriginal;
    @JsonProperty("poster_path")
    private String imagemPoster;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("popularity")
    private Double popularidade;
    @JsonProperty("first_air_date")
    private String primeiraDataExibicao;
    @JsonProperty("last_air_date")
    private String ultimaDataExibicao;
    @JsonProperty("number_of_seasons")
    private Integer quantidadeTemporadas;
    private List<GeneroTMDBDto> genres;
    private String overview;
}
