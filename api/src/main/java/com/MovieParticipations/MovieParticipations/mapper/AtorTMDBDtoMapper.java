package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBMovieDto;
import com.MovieParticipations.MovieParticipations.dto.AtorTMDBSerieDto;
import com.MovieParticipations.MovieParticipations.dto.TMDBDto;

public class AtorTMDBDtoMapper {
    public static AtorTMDBDto toDtoFromSerie(AtorTMDBSerieDto atorTMDBSerieDto) {
        return AtorTMDBDto.builder()
                .character(atorTMDBSerieDto.getRoles().get(0).getCharacter())
                .known_for_department(atorTMDBSerieDto.getKnown_for_department())
                .name(atorTMDBSerieDto.getName())
                .profile_path(atorTMDBSerieDto.getProfile_path())
                .popularity(atorTMDBSerieDto.getPopularity())
                .build();
    }

    public static AtorTMDBDto toDtoFromMovie(AtorTMDBMovieDto atorTMDBMovieDto) {
        return AtorTMDBDto.builder()
                .profile_path(atorTMDBMovieDto.getProfile_path())
                .name(atorTMDBMovieDto.getName())
                .known_for_department(atorTMDBMovieDto.getKnown_for_department())
                .character(atorTMDBMovieDto.getCharacter())
                .popularity(atorTMDBMovieDto.getPopularity())
                .build();
    }
}
