package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;
import com.MovieParticipations.MovieParticipations.dto.CastResponseDtoMovie;

import java.util.List;

public class CastResponseDtoMovieFactory {

    public static CastResponseDtoMovie getCastResponseDtoMovie(List<AtorTMDBMovieDto> elenco) {
        return CastResponseDtoMovie.builder()
                .elenco(elenco)
                .build();
    }
}
