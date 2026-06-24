package com.MovieParticipations.MovieParticipations.factories.tmdb;

import com.MovieParticipations.MovieParticipations.dto.AtorTMDBDtoPesquisaId;

public class AtorTMDBDtoPesquisaIdFactory {

    public static AtorTMDBDtoPesquisaId getKeanuReevesTMDBDto() {
        Long id = 6384L;
        String nome = "Keanu Reeves";
        String conhecidoPor = "Acting";
        Double popularidade = 10.0;
        String fotoDePerfil = "/keanu.jpg";

        return AtorTMDBDtoPesquisaId.builder()
                .id(id)
                .nome(nome)
                .conhecidoPor(conhecidoPor)
                .popularidade(popularidade)
                .fotoDePerfil(fotoDePerfil)
                .build();
    }
}
