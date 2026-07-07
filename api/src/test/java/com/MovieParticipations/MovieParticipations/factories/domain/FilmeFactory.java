package com.MovieParticipations.MovieParticipations.factories.domain;

import com.MovieParticipations.MovieParticipations.domain.Filme;

import java.time.LocalDate;

public class FilmeFactory {
    private static final int ANO_ULTIMA_ATUALIZACAO = 2024;
    private static final int MES_ULTIMA_ATUALIZACAO = 1;
    private static final int DIA_ULTIMA_ATUALIZACAO = 1;
    private static final int ANO_LANCAMENTO_MATRIX = 2011;
    private static final int MES_LANCAMENTO_MATRIX = 2;
    private static final int DIA_LANCAMENTO_MATRIX = 10;
    private static final LocalDate DATA_LANCAMENTO_MATRIX = LocalDate.of(
            ANO_LANCAMENTO_MATRIX,
            MES_LANCAMENTO_MATRIX,
            DIA_LANCAMENTO_MATRIX
    );

    public static Filme getFilmeEntity() {
        Long idTmdb = 123L;
        String titulo = "Filme Teste";
        String tituloNormalizado = "filme teste";
        String imagem = "/filme-teste.jpg";
        String overview = "Um filme usado para testes.";
        LocalDate ultimaAtualizacao = LocalDate.of(ANO_ULTIMA_ATUALIZACAO, MES_ULTIMA_ATUALIZACAO, DIA_ULTIMA_ATUALIZACAO);
        Double popularidade = 10.0;
        Boolean elencoInicializado = true;
        Boolean infoAtualizado = true;

        return Filme.builder()
                .idTmdb(idTmdb)
                .titulo(titulo)
                .tituloNormalizado(tituloNormalizado)
                .imagem(imagem)
                .overview(overview)
                .ultimaAtualizacao(ultimaAtualizacao)
                .dataLancamento(DATA_LANCAMENTO_MATRIX)
                .popularidade(popularidade)
                .elencoInicializado(elencoInicializado)
                .infoAtualizado(infoAtualizado)
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
        String overview = "Um hacker descobre que a realidade e uma simulacao.";
        LocalDate ultimaAtualizacao = LocalDate.of(ANO_ULTIMA_ATUALIZACAO, MES_ULTIMA_ATUALIZACAO, DIA_ULTIMA_ATUALIZACAO);
        Double popularidade = 10.0;
        Boolean elencoInicializado = true;
        Boolean infoAtualizado = true;

        return Filme.builder()
                .idTmdb(idTmdb)
                .titulo(titulo)
                .tituloNormalizado(tituloNormalizado)
                .imagem(imagem)
                .overview(overview)
                .ultimaAtualizacao(ultimaAtualizacao)
                .dataLancamento(DATA_LANCAMENTO_MATRIX)
                .popularidade(popularidade)
                .elencoInicializado(elencoInicializado)
                .infoAtualizado(infoAtualizado)
                .build();
    }

    public static Filme getMatrixFilmeEntityComId() {
        Long id = 1L;
        Filme filme = getMatrixFilmeEntity();
        filme.setId(id);
        return filme;
    }
}
