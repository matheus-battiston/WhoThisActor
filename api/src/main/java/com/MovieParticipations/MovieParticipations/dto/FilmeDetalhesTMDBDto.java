package com.MovieParticipations.MovieParticipations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FilmeDetalhesTMDBDto {
    private Long id;
    @JsonProperty("poster_path")
    private String imagemPoster;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("title")
    private String titulo;
    @JsonProperty("original_title")
    private String tituloOriginal;
    @JsonProperty("popularity")
    private Double popularidade;
    @JsonProperty("release_date")
    private LocalDate dataLancamento;
    private List<GeneroTMDBDto> genres;
    private String overview;
}
