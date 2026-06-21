package com.MovieParticipations.MovieParticipations.factories.response;

import com.MovieParticipations.MovieParticipations.controller.response.OpcaoPesquisaElencoResponse;

public class OpcaoPesquisaElencoResponseFactory {

    public static OpcaoPesquisaElencoResponse getOpcaoPesquisaElencoResponse() {
        Long id = 99L;
        String nome = "Ator Teste";
        String nomePersonagem = "Personagem Teste";
        String urlImagem = "/ator-teste.jpg";
        Double popularity = 10.0;

        return OpcaoPesquisaElencoResponse.builder()
                .id(id)
                .nome(nome)
                .nomePersonagem(nomePersonagem)
                .urlImagem(urlImagem)
                .popularity(popularity)
                .build();
    }

    public static OpcaoPesquisaElencoResponse getNeoOpcaoPesquisaElencoResponse() {
        Long id = 99L;
        String nome = "Keanu Reeves";
        String nomePersonagem = "Neo";
        String urlImagem = "/keanu.jpg";
        Double popularity = 10.0;

        return OpcaoPesquisaElencoResponse.builder()
                .id(id)
                .nome(nome)
                .nomePersonagem(nomePersonagem)
                .urlImagem(urlImagem)
                .popularity(popularity)
                .build();
    }

    public static OpcaoPesquisaElencoResponse getWalterWhiteOpcaoPesquisaElencoResponse() {
        Long id = 100L;
        String nome = "Bryan Cranston";
        String nomePersonagem = "Walter White";
        String urlImagem = "/bryan.jpg";
        Double popularity = 9.5;

        return OpcaoPesquisaElencoResponse.builder()
                .id(id)
                .nome(nome)
                .nomePersonagem(nomePersonagem)
                .urlImagem(urlImagem)
                .popularity(popularity)
                .build();
    }
}
