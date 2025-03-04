package com.MovieParticipations.MovieParticipations.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CastResponseDtoSerie {
    List<AtorTMDBSerieDto> cast;
}
