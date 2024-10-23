package com.example.spaceship.config.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for setting up caching in the application.
 * This class enables caching using Caffeine, a high-performance caching library.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Creates and configures a CaffeineCacheManager bean.
     * The Caffeine cache manager is configured with the following settings:
     * - Cache entries will expire 10 seconds after they are written.
     * - The maximum size of the cache is set to 100 entries.
     * @return a configured CaffeineCacheManager instance.
     */
    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(100));
        return cacheManager;
    }
}
