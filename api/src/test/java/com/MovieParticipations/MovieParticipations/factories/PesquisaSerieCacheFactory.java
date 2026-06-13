package com.MovieParticipations.MovieParticipations.factories;

import com.MovieParticipations.MovieParticipations.domain.PesquisaSerieCache;

import java.time.LocalDate;

public class PesquisaSerieCacheFactory {

    public static PesquisaSerieCache get() {
        String termoNormalizado = "breaking bad";
        LocalDate ultimaSincronizacao = LocalDate.of(2024, 1, 1);

        return PesquisaSerieCache.builder()
                .termoNormalizado(termoNormalizado)
                .ultimaSincronizacao(ultimaSincronizacao)
                .build();
    }

    public static PesquisaSerieCache getComId(Long id) {
        PesquisaSerieCache cache = get();
        cache.setId(id);
        return cache;
    }
}
