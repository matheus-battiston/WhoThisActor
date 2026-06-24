package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.ListaProducoesTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.ProducaoTMDBDto;

import java.util.List;

public class ListaProducoesTMDBDtoFactory {

    public static ListaProducoesTMDBDto getListaProducoesTMDBDto(List<ProducaoTMDBDto> elenco) {
        return ListaProducoesTMDBDto.builder()
                .elenco(elenco)
                .build();
    }
}
