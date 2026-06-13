package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.Ator;

import java.time.LocalDate;

public class AtorFactory {

    public static Ator get() {
        Long idTmdb = 999L;
        String nome = "Ator Teste";
        String nomeNormalizado = "ator teste";
        String imagem = "/ator-teste.jpg";
        Double popularity = 10.0;
        LocalDate ultimaAtualizacao = LocalDate.of(2024, 1, 1);
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

    public static Ator getComId(Long id) {
        Ator ator = get();
        ator.setId(id);
        return ator;
    }

    public static Ator getKeanuReeves() {
        Long idTmdb = 6384L;
        String nome = "Keanu Reeves";
        String nomeNormalizado = "keanu reeves";
        String imagem = "/keanu.jpg";
        Double popularity = 10.0;
        LocalDate ultimaAtualizacao = LocalDate.of(2024, 1, 1);
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

    public static Ator getKeanuReevesComId() {
        Long id = 99L;
        Ator ator = getKeanuReeves();
        ator.setId(id);
        return ator;
    }

    public static Ator getBryanCranston() {
        Long idTmdb = 17419L;
        String nome = "Bryan Cranston";
        String nomeNormalizado = "bryan cranston";
        String imagem = "/bryan.jpg";
        Double popularity = 9.5;
        LocalDate ultimaAtualizacao = LocalDate.of(2024, 1, 1);
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

    public static Ator getBryanCranstonComId() {
        Long id = 100L;
        Ator ator = getBryanCranston();
        ator.setId(id);
        return ator;
    }
}
