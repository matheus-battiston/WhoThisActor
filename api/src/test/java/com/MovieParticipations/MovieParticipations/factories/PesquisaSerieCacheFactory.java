package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.PesquisaSerieCache;

import java.time.LocalDate;

public class PesquisaSerieCacheFactory {
    private static final int ANO_ULTIMA_SINCRONIZACAO = 2024;
    private static final int MES_ULTIMA_SINCRONIZACAO = 1;
    private static final int DIA_ULTIMA_SINCRONIZACAO = 1;

    public static PesquisaSerieCache getPesquisaSerieCacheEntity() {
        String termoNormalizado = "breaking bad";
        LocalDate ultimaSincronizacao = LocalDate.of(ANO_ULTIMA_SINCRONIZACAO, MES_ULTIMA_SINCRONIZACAO, DIA_ULTIMA_SINCRONIZACAO);

        return PesquisaSerieCache.builder()
                .termoNormalizado(termoNormalizado)
                .ultimaSincronizacao(ultimaSincronizacao)
                .build();
    }

    public static PesquisaSerieCache getPesquisaSerieCacheEntityComId(Long id) {
        PesquisaSerieCache cache = getPesquisaSerieCacheEntity();
        cache.setId(id);
        return cache;
    }
}
