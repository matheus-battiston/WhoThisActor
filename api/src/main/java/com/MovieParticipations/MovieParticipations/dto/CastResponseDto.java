package com.MovieParticipations.MovieParticipations.dto;

import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CastResponseDto {
    private List<AtorTMDBDto> cast;

}