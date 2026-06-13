package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.Serie;

import java.time.LocalDate;

public class SerieFactory {

    public static Serie get() {
        Long idTmdb = 456L;
        String titulo = "Serie Teste";
        String tituloNormalizado = "serie teste";
        String imagem = "/serie-teste.jpg";
        LocalDate ultimaAtualizacao = LocalDate.of(2024, 1, 1);
        Double popularidade = 9.0;
        Boolean inicializado = true;

        return Serie.builder()
                .idTmdb(idTmdb)
                .titulo(titulo)
                .tituloNormalizado(tituloNormalizado)
                .imagem(imagem)
                .ultimaAtualizacao(ultimaAtualizacao)
                .popularidade(popularidade)
                .inicializado(inicializado)
                .build();
    }

    public static Serie getComId(Long id) {
        Serie serie = get();
        serie.setId(id);
        return serie;
    }

    public static Serie getBreakingBad() {
        Long idTmdb = 456L;
        String titulo = "Breaking Bad";
        String tituloNormalizado = "breaking bad";
        String imagem = "/breaking-bad.jpg";
        LocalDate ultimaAtualizacao = LocalDate.of(2024, 1, 1);
        Double popularidade = 9.5;
        Boolean inicializado = true;

        return Serie.builder()
                .idTmdb(idTmdb)
                .titulo(titulo)
                .tituloNormalizado(tituloNormalizado)
                .imagem(imagem)
                .ultimaAtualizacao(ultimaAtualizacao)
                .popularidade(popularidade)
                .inicializado(inicializado)
                .build();
    }

    public static Serie getBreakingBadComId() {
        Long id = 2L;
        Serie serie = getBreakingBad();
        serie.setId(id);
        return serie;
    }
}
