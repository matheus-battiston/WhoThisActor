package com.MovieParticipations.MovieParticipations.dto;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AtorTMDBMovieDto {
    String name;
    String profile_path;
    String character;
    String known_for_department;
    Double popularity;
}

