package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.Serie;

import java.time.LocalDate;

public class SerieFactory {
    private static final int ANO_ULTIMA_ATUALIZACAO = 2024;
    private static final int MES_ULTIMA_ATUALIZACAO = 1;
    private static final int DIA_ULTIMA_ATUALIZACAO = 1;

    public static Serie getSerieEntity() {
        Long idTmdb = 456L;
        String titulo = "Serie Teste";
        String tituloNormalizado = "serie teste";
        String imagem = "/serie-teste.jpg";
        LocalDate ultimaAtualizacao = LocalDate.of(ANO_ULTIMA_ATUALIZACAO, MES_ULTIMA_ATUALIZACAO, DIA_ULTIMA_ATUALIZACAO);
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

    public static Serie getSerieEntityComId(Long id) {
        Serie serie = getSerieEntity();
        serie.setId(id);
        return serie;
    }

    public static Serie getBreakingBadSerieEntity() {
        Long idTmdb = 456L;
        String titulo = "Breaking Bad";
        String tituloNormalizado = "breaking bad";
        String imagem = "/breaking-bad.jpg";
        LocalDate ultimaAtualizacao = LocalDate.of(ANO_ULTIMA_ATUALIZACAO, MES_ULTIMA_ATUALIZACAO, DIA_ULTIMA_ATUALIZACAO);
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

    public static Serie getBreakingBadSerieEntityComId() {
        Long id = 2L;
        Serie serie = getBreakingBadSerieEntity();
        serie.setId(id);
        return serie;
    }
}
