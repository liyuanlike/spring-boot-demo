package com.github.util;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.github.service.PublisherService;
import com.github.service.UserMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/** Bean配置管理 */
@Configuration
public class BeanConfig {

	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		return new StringHttpMessageConverter(StandardCharsets.UTF_8);
	}
	/* 支持fastjson */
	@Bean
	@ConditionalOnClass(FastJsonConfig.class)
	public FastJsonConfig fastJsonConfig() {
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.PrettyFormat
		);
		return fastJsonConfig;
	}
	@Bean
	@ConditionalOnClass({FastJsonHttpMessageConverter.class})
	public HttpMessageConverters fastJsonHttpMessageConverters(FastJsonConfig fastJsonConfig, StringHttpMessageConverter stringHttpMessageConverter) {

		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		fastJsonHttpMessageConverter.setSupportedMediaTypes(new ArrayList<MediaType>() {
			private static final long serialVersionUID = 8724659690562975474L;
			{
			this.add(MediaType.APPLICATION_JSON);
			this.add(MediaType.APPLICATION_JSON_UTF8);
		}});

		return new HttpMessageConverters(fastJsonHttpMessageConverter, stringHttpMessageConverter);
	}


	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);

		GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
		template.setDefaultSerializer(fastJsonRedisSerializer);

		return template;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, UserMessageListener userMessageListener) {

		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
		redisMessageListenerContainer.addMessageListener(userMessageListener, new ChannelTopic(PublisherService.channel));
		return redisMessageListenerContainer;
	}

}

