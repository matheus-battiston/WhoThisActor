package com.MovieParticipations.MovieParticipations.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FilmeTMDBDto {
    Long id;
    String poster_path;
    String title;
    Long popularity;
}
