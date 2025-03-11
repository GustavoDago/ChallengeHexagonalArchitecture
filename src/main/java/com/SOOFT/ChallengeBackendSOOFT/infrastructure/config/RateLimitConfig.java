package com.SOOFT.ChallengeBackendSOOFT.infrastructure.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {
    @Bean
    public Bucket bucket() {
        // Permite 10 solicitudes cada minuto.
        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(10, refill); //10 tokens, que se recargan cada 1 minuto
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
