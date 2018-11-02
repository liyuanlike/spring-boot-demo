package com.github;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
@EnableCaching
public class CaffeineCacheConfig {

    @Bean
    public CacheManager cacheManagerWithCacheLoading(){

        Caffeine caffeine = Caffeine.newBuilder()
                .recordStats()
                .initialCapacity(128)
                .maximumSize(1024) // maximumSize用来控制cache的最大缓存数量，maximumSize和maximumWeight不可以同时使用
//                .refreshAfterWrite(5, TimeUnit.SECONDS) // refreshAfterWrite必须设置cacheLoad方法
                .expireAfterAccess(50, TimeUnit.SECONDS);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setAllowNullValues(true);
        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheLoader(cacheLoader);
//        cacheManager.setCacheNames(getNames());
        return cacheManager;
    }

}
