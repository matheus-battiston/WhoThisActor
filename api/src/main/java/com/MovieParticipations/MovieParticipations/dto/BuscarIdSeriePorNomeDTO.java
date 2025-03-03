package com.MovieParticipations.MovieParticipations.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuscarIdSeriePorNomeDTO {
    List<SerieTMDBDto> results;
}
