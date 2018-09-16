package com.github.util;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

}

