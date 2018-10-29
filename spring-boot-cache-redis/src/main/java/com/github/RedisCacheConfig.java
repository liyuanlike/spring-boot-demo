package com.github;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

@EnableCaching
@Configuration
public class RedisCacheConfig {

	@Bean
	public CacheManagerCustomizer<RedisCacheManager> cacheManagerCustomizer() {
		return cacheManager -> {
			System.err.println(cacheManager.getCacheConfigurations());
			cacheManager.setTransactionAware(true);
		};
	}

}
