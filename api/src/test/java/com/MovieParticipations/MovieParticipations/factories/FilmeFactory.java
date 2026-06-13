package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.Filme;

import java.time.LocalDate;

public class FilmeFactory {

    public static Filme get() {
        Long idTmdb = 123L;
        String titulo = "Filme Teste";
        String tituloNormalizado = "filme teste";
        String imagem = "/filme-teste.jpg";
        LocalDate ultimaAtualizacao = LocalDate.of(2024, 1, 1);
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

    public static Filme getComId(Long id) {
        Filme filme = get();
        filme.setId(id);
        return filme;
    }

    public static Filme getMatrix() {
        Long idTmdb = 123L;
        String titulo = "Matrix";
        String tituloNormalizado = "matrix";
        String imagem = "/matrix.jpg";
        LocalDate ultimaAtualizacao = LocalDate.of(2024, 1, 1);
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

    public static Filme getMatrixComId() {
        Long id = 1L;
        Filme filme = getMatrix();
        filme.setId(id);
        return filme;
    }
}
