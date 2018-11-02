package com.github;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

		GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer(); // 替换默认的JdkSerializationRedisSerializer
		RedisSerializationContext.SerializationPair<Object> valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);
		RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
				.serializeValuesWith(valueSerializationPair)
				.entryTtl(Duration.ofMinutes(10)).prefixKeysWith("redis:object:cache:");

		Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
		redisCacheConfigurationMap.put("user", cacheConfig);
		redisCacheConfigurationMap.put("objectCache", cacheConfig);

		RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
				.withInitialCacheConfigurations(redisCacheConfigurationMap)
				.transactionAware() // 启用关联数据库事务
				.build();

		return cacheManager;
	}

}
