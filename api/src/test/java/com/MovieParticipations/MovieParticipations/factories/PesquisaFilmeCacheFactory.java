package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.PesquisaFilmeCache;

import java.time.LocalDate;

public class PesquisaFilmeCacheFactory {

    public static PesquisaFilmeCache get() {
        String termoNormalizado = "matrix";
        LocalDate ultimaSincronizacao = LocalDate.of(2024, 1, 1);

        return PesquisaFilmeCache.builder()
                .termoNormalizado(termoNormalizado)
                .ultimaSincronizacao(ultimaSincronizacao)
                .build();
    }

    public static PesquisaFilmeCache getComId(Long id) {
        PesquisaFilmeCache cache = get();
        cache.setId(id);
        return cache;
    }
}
