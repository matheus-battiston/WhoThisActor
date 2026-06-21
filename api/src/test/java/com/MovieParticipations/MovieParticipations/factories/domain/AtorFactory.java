package com.MovieParticipations.MovieParticipations.factories.domain;

import com.MovieParticipations.MovieParticipations.domain.Ator;

import java.time.LocalDate;

public class AtorFactory {
    private static final int ANO_ULTIMA_ATUALIZACAO = 2024;
    private static final int MES_ULTIMA_ATUALIZACAO = 1;
    private static final int DIA_ULTIMA_ATUALIZACAO = 1;

    public static Ator getAtorEntity() {
        Long idTmdb = 999L;
        String nome = "Ator Teste";
        String nomeNormalizado = "ator teste";
        String imagem = "/ator-teste.jpg";
        Double popularity = 10.0;
        LocalDate ultimaAtualizacao = LocalDate.of(ANO_ULTIMA_ATUALIZACAO, MES_ULTIMA_ATUALIZACAO, DIA_ULTIMA_ATUALIZACAO);
        Boolean inicializado = true;

        return Ator.builder()
                .idTmdb(idTmdb)
                .nome(nome)
                .nomeNormalizado(nomeNormalizado)
                .imagem(imagem)
                .popularity(popularity)
                .ultimaAtualizacao(ultimaAtualizacao)
                .inicializado(inicializado)
                .build();
    }

    public static Ator getAtorEntityComId(Long id) {
        Ator ator = getAtorEntity();
        ator.setId(id);
        return ator;
    }

    public static Ator getKeanuReevesAtorEntity() {
        Long idTmdb = 6384L;
        String nome = "Keanu Reeves";
        String nomeNormalizado = "keanu reeves";
        String imagem = "/keanu.jpg";
        Double popularity = 10.0;
        LocalDate ultimaAtualizacao = LocalDate.of(ANO_ULTIMA_ATUALIZACAO, MES_ULTIMA_ATUALIZACAO, DIA_ULTIMA_ATUALIZACAO);
        Boolean inicializado = true;

        return Ator.builder()
                .idTmdb(idTmdb)
                .nome(nome)
                .nomeNormalizado(nomeNormalizado)
                .imagem(imagem)
                .popularity(popularity)
                .ultimaAtualizacao(ultimaAtualizacao)
                .inicializado(inicializado)
                .build();
    }

    public static Ator getKeanuReevesAtorEntityComId() {
        Long id = 99L;
        Ator ator = getKeanuReevesAtorEntity();
        ator.setId(id);
        return ator;
    }

    public static Ator getBryanCranstonAtorEntity() {
        Long idTmdb = 17419L;
        String nome = "Bryan Cranston";
        String nomeNormalizado = "bryan cranston";
        String imagem = "/bryan.jpg";
        Double popularity = 9.5;
        LocalDate ultimaAtualizacao = LocalDate.of(ANO_ULTIMA_ATUALIZACAO, MES_ULTIMA_ATUALIZACAO, DIA_ULTIMA_ATUALIZACAO);
        Boolean inicializado = true;

        return Ator.builder()
                .idTmdb(idTmdb)
                .nome(nome)
                .nomeNormalizado(nomeNormalizado)
                .imagem(imagem)
                .popularity(popularity)
                .ultimaAtualizacao(ultimaAtualizacao)
                .inicializado(inicializado)
                .build();
    }

    public static Ator getBryanCranstonAtorEntityComId() {
        Long id = 100L;
        Ator ator = getBryanCranstonAtorEntity();
        ator.setId(id);
        return ator;
    }
}
