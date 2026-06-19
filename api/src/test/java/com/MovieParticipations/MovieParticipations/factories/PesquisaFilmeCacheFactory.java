package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.PesquisaFilmeCache;

import java.time.LocalDate;

public class PesquisaFilmeCacheFactory {
    private static final int ANO_ULTIMA_SINCRONIZACAO = 2024;
    private static final int MES_ULTIMA_SINCRONIZACAO = 1;
    private static final int DIA_ULTIMA_SINCRONIZACAO = 1;

    public static PesquisaFilmeCache getPesquisaFilmeCacheEntity() {
        String termoNormalizado = "matrix";
        LocalDate ultimaSincronizacao = LocalDate.of(ANO_ULTIMA_SINCRONIZACAO, MES_ULTIMA_SINCRONIZACAO, DIA_ULTIMA_SINCRONIZACAO);

        return PesquisaFilmeCache.builder()
                .termoNormalizado(termoNormalizado)
                .ultimaSincronizacao(ultimaSincronizacao)
                .build();
    }

    public static PesquisaFilmeCache getPesquisaFilmeCacheEntityComId(Long id) {
        PesquisaFilmeCache cache = getPesquisaFilmeCacheEntity();
        cache.setId(id);
        return cache;
    }
}
