package com.MovieParticipations.MovieParticipations.mapper;

import com.MovieParticipations.MovieParticipations.domain.TipoMidia;
import com.MovieParticipations.MovieParticipations.dto.FilmeTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.SerieTMDBDto;
import com.MovieParticipations.MovieParticipations.dto.TMDBDto;
import com.fasterxml.jackson.databind.JsonNode;

import static com.MovieParticipations.MovieParticipations.domain.TipoMidia.*;

public class TMDBDtoMapper {
    private static final String ID = "id";
    private static final String IMAGEM = "poster_path";
    private static final String NOME = "name";
    private static final String TITULO = "title";

    public static TMDBDto toTMDBDto(JsonNode primeiroResultado, TipoMidia tipo) {
        String parametroNome = tipo.equals(TV) ? NOME : TITULO;
        return TMDBDto.builder()
                .id(primeiroResultado.get(ID).asLong())
                .tipoProducao(tipo)
                .image(primeiroResultado.get(IMAGEM).asText())
                .name(primeiroResultado.get(parametroNome).asText())
                .build();
    }

    public static TMDBDto toTMDBDtoFromSerie(SerieTMDBDto serieTMDBDto, TipoMidia tipo) {
        return TMDBDto.builder()
                .id(serieTMDBDto.getId())
                .image(serieTMDBDto.getPoster_path())
                .name(serieTMDBDto.getName())
                .tipoProducao(tipo)
                .build();
    }

    public static TMDBDto toTMDBDtoFromFilme(FilmeTMDBDto filmeTMDBDto, TipoMidia tipo) {
        return TMDBDto.builder()
                .image(filmeTMDBDto.getPoster_path())
                .id(filmeTMDBDto.getId())
                .tipoProducao(tipo)
                .name(filmeTMDBDto.getTitle())
                .build();
    }
}
