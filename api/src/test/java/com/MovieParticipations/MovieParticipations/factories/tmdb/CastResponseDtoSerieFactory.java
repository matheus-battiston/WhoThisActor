package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDtoSerie;

import java.util.List;

public class CastResponseDtoSerieFactory {

    public static CastResponseDtoSerie getCastResponseDtoSerie(List<AtorTMDBSerieDto> elenco) {
        return CastResponseDtoSerie.builder()
                .elenco(elenco)
                .build();
    }
}
