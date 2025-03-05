package com.MovieParticipations.MovieParticipations.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProducaoTMDBDto {
    Long id;
    String title;
    String name;
    String media_type;
    Long popularity;
    String poster_path;
    String character;
}
