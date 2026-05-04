package com.MovieParticipations.MovieParticipations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecarregarCacheClassificacaoService {

    @Value("${CLASSIFY_CACHE_RELOAD_ADDRESS}")
    private String classifyCacheReloadAddress;

    private final RestTemplate restTemplate;

    public void recarregarCacheAtoresPorProducao() {
        try {
            restTemplate.exchange(
                    classifyCacheReloadAddress,
                    POST,
                    null,
                    String.class
            );
        } catch (Exception e) {
            log.warn("Nao foi possivel recarregar o cache de atores por producao do classify", e);
        }
    }
}
