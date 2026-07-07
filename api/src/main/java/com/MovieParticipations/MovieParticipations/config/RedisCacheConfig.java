package com.MovieParticipations.MovieParticipations.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {
    private static final String PROVIDERS_CACHE = "providers";
    private static final String BUSCA_INICIAL_CACHE = "buscaInicial";

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory,
            @Value("${providers.cache.ttl:24h}") Duration providersCacheTtl,
            @Value("${busca-inicial.cache.ttl:24h}") Duration buscaInicialCacheTtl
    ) {
        RedisCacheConfiguration providersCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(providersCacheTtl);
        RedisCacheConfiguration buscaInicialCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(buscaInicialCacheTtl);

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(providersCacheConfiguration)
                .withCacheConfiguration(PROVIDERS_CACHE, providersCacheConfiguration)
                .withCacheConfiguration(BUSCA_INICIAL_CACHE, buscaInicialCacheConfiguration)
                .build();
    }
}
