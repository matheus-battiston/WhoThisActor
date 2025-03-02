package com.MovieParticipations.MovieParticipations.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SerieTMDBDto {
    Long id;
    String name;
    String poster_path;
    Long popularity;
}
