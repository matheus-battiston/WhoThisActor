package com.MovieParticipations.MovieParticipations.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("RedisCacheConfig")
class RedisCacheConfigTest {
    private static final long TTL_PROVIDERS_EM_HORAS = 12L;
    private static final long TTL_BUSCA_INICIAL_EM_HORAS = 6L;
    private static final String CACHE_PROVIDERS = "providers";
    private static final String CACHE_BUSCA_INICIAL = "buscaInicial";

    @Test
    @DisplayName("Deve configurar cache de providers e busca inicial com seus TTLs")
    void deveConfigurarCacheDeProvidersEBuscaInicialComSeusTtls() {
        Duration providersTtl = Duration.ofHours(TTL_PROVIDERS_EM_HORAS);
        Duration buscaInicialTtl = Duration.ofHours(TTL_BUSCA_INICIAL_EM_HORAS);
        RedisCacheConfig config = new RedisCacheConfig();

        RedisCacheManager cacheManager = config.cacheManager(
                mock(RedisConnectionFactory.class),
                providersTtl,
                buscaInicialTtl
        );
        cacheManager.afterPropertiesSet();

        Map<String, RedisCacheConfiguration> cacheConfigurations = cacheManager.getCacheConfigurations();
        assertThat(cacheConfigurations).containsKeys(CACHE_PROVIDERS, CACHE_BUSCA_INICIAL);
        assertThat(cacheConfigurations.get(CACHE_PROVIDERS).getTtlFunction().getTimeToLive(null, null))
                .isEqualTo(providersTtl);
        assertThat(cacheConfigurations.get(CACHE_BUSCA_INICIAL).getTtlFunction().getTimeToLive(null, null))
                .isEqualTo(buscaInicialTtl);
    }
}
