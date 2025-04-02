package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.controller.response.SerieInfoResponse;
import com.MovieParticipations.MovieParticipations.domain.Serie;
import com.MovieParticipations.MovieParticipations.dto.ProviderDto;
import com.MovieParticipations.MovieParticipations.dto.TMDBDto;

import java.time.LocalDate;
import java.util.List;

public class SerieMapper {
    public static Serie toEntity(TMDBDto dto) {
        return Serie.builder()
                .imagem(dto.getImage())
                .titulo(dto.getName())
                .tipo(dto.getTipoProducao())
                .idTmdb(dto.getId())
                .ultimaAtualizacao(LocalDate.now())
                .build();
    }

    public static SerieInfoResponse toResponse(Serie serie, List<ProviderDto> providers) {
        return SerieInfoResponse.builder()
                .nome(serie.getTitulo())
                .tipoMidia(serie.getTipo())
                .imagem(serie.getImagem())
                .providers(providers)
                .build();
    }
}