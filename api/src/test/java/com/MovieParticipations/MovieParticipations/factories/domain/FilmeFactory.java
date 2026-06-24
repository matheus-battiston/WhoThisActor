package com.MovieParticipations.MovieParticipations.factories.domain;

import com.MovieParticipations.MovieParticipations.domain.Filme;

import java.time.LocalDate;

public class FilmeFactory {
    private static final int ANO_ULTIMA_ATUALIZACAO = 2024;
    private static final int MES_ULTIMA_ATUALIZACAO = 1;
    private static final int DIA_ULTIMA_ATUALIZACAO = 1;

    public static Filme getFilmeEntity() {
        Long idTmdb = 123L;
        String titulo = "Filme Teste";
        String tituloNormalizado = "filme teste";
        String imagem = "/filme-teste.jpg";
        LocalDate ultimaAtualizacao = LocalDate.of(ANO_ULTIMA_ATUALIZACAO, MES_ULTIMA_ATUALIZACAO, DIA_ULTIMA_ATUALIZACAO);
        Double popularidade = 10.0;
        Boolean inicializado = true;

        return Filme.builder()
                .idTmdb(idTmdb)
                .titulo(titulo)
                .tituloNormalizado(tituloNormalizado)
                .imagem(imagem)
                .ultimaAtualizacao(ultimaAtualizacao)
                .popularidade(popularidade)
                .inicializado(inicializado)
                .build();
    }

    public static Filme getFilmeEntityComId(Long id) {
        Filme filme = getFilmeEntity();
        filme.setId(id);
        return filme;
    }

    public static Filme getMatrixFilmeEntity() {
        Long idTmdb = 123L;
        String titulo = "Matrix";
        String tituloNormalizado = "matrix";
        String imagem = "/matrix.jpg";
        LocalDate ultimaAtualizacao = LocalDate.of(ANO_ULTIMA_ATUALIZACAO, MES_ULTIMA_ATUALIZACAO, DIA_ULTIMA_ATUALIZACAO);
        Double popularidade = 10.0;
        Boolean inicializado = true;

        return Filme.builder()
                .idTmdb(idTmdb)
                .titulo(titulo)
                .tituloNormalizado(tituloNormalizado)
                .imagem(imagem)
                .ultimaAtualizacao(ultimaAtualizacao)
                .popularidade(popularidade)
                .inicializado(inicializado)
                .build();
    }

    public static Filme getMatrixFilmeEntityComId() {
        Long id = 1L;
        Filme filme = getMatrixFilmeEntity();
        filme.setId(id);
        return filme;
    }
}
