package com.MovieParticipations.MovieParticipations.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FilmeTMDBDto {
    Long id;
    @JsonProperty("poster_path")
    String imagemPoster;
    @JsonProperty("title")
    String titulo;
    @JsonProperty("popularity")
    Double popularidade;
    @JsonProperty("release_date")
    LocalDate dataLancamento;
    @JsonProperty("original_title")
    String tituloOriginal;
}
