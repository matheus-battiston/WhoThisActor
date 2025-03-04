package com.MovieParticipations.MovieParticipations.dto;

import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CastResponseDtoMovie {
    private List<AtorTMDBMovieDto> cast;

}