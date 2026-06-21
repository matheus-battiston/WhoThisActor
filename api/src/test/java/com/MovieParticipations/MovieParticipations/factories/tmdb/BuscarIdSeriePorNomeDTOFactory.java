package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.BuscarIdSeriePorNomeDTO;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;

import java.util.List;

public class BuscarIdSeriePorNomeDTOFactory {

    public static BuscarIdSeriePorNomeDTO getBuscarIdSeriePorNomeDTO(List<SerieTMDBDto> resultados) {
        return BuscarIdSeriePorNomeDTO.builder()
                .resultados(resultados)
                .build();
    }
}
