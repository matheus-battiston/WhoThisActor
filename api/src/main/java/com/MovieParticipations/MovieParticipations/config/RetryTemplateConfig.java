package com.MovieParticipations.MovieParticipations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

@Configuration
public class RetryTemplateConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        org.springframework.retry.support.RetryTemplate retryTemplate = new org.springframework.retry.support.RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(
                3,
                Map.of(
                        ResourceAccessException.class, true,
                        HttpServerErrorException.class, true
                )
        );

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(2000);
        backOffPolicy.setMultiplier(2);
        backOffPolicy.setMaxInterval(5000);

        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}