package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.dto.ProviderDto;

public class ProviderDtoFactory {

    public static ProviderDto get() {
        int idProvider = 1;
        String nomeProvider = "Provider Teste";
        String imagemLogo = "/provider-teste.jpg";
        int prioridade = 1;

        return ProviderDto.builder()
                .idProvider(idProvider)
                .nomeProvider(nomeProvider)
                .imagemLogo(imagemLogo)
                .prioridade(prioridade)
                .build();
    }

    public static ProviderDto getNetflix() {
        int idProvider = 8;
        String nomeProvider = "Netflix";
        String imagemLogo = "/netflix.jpg";
        int prioridade = 1;

        return ProviderDto.builder()
                .idProvider(idProvider)
                .nomeProvider(nomeProvider)
                .imagemLogo(imagemLogo)
                .prioridade(prioridade)
                .build();
    }

    public static ProviderDto getDisneyPlus() {
        int idProvider = 337;
        String nomeProvider = "Disney+";
        String imagemLogo = "/disney-plus.jpg";
        int prioridade = 2;

        return ProviderDto.builder()
                .idProvider(idProvider)
                .nomeProvider(nomeProvider)
                .imagemLogo(imagemLogo)
                .prioridade(prioridade)
                .build();
    }
}
