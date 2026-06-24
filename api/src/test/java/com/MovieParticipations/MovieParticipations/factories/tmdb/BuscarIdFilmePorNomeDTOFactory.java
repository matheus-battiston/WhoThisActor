package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.BuscarIdFilmePorNomeDTO;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;

import java.util.List;

public class BuscarIdFilmePorNomeDTOFactory {

    public static BuscarIdFilmePorNomeDTO getBuscarIdFilmePorNomeDTO(List<FilmeTMDBDto> resultados) {
        return BuscarIdFilmePorNomeDTO.builder()
                .resultados(resultados)
                .build();
    }
}
