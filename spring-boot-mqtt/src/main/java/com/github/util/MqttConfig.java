package com.github.util;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


@Configuration
public class MqttConfig {

	// 配置client factory
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {

		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

		MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
		mqttConnectOptions.setServerURIs(new String[]{"tcp://182.151.25.46:1883"});
		mqttConnectOptions.setUserName("admin");
		mqttConnectOptions.setPassword("admin".toCharArray());
        factory.setConnectionOptions(mqttConnectOptions);
		return factory;
	}


	// 配置consumer
	@Bean
	public IntegrationFlow mqttInFlow() {
		return IntegrationFlows.from(mqttInbound())
				.transform(p -> p + ", received from MQTT")
				.handle(logger())
				.get();
	}

	private LoggingHandler logger() {
		LoggingHandler loggingHandler = new LoggingHandler("INFO");
		loggingHandler.setLoggerName("siSample");
		return loggingHandler;
	}

	@Bean
	public MessageProducerSupport mqttInbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("siSampleConsumer",
				mqttClientFactory(), "siSampleTopic");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		return adapter;
	}



	// 配置producer
	@Bean
	public IntegrationFlow mqttOutFlow() {
		//console input
//        return IntegrationFlows.from(CharacterStreamReadingMessageSource.stdin(),
//                e -> e.poller(Pollers.fixedDelay(1000)))
//                .transform(p -> p + " sent to MQTT")
//                .handle(mqttOutbound())
//                .get();
		return IntegrationFlows.from(outChannel())
				.handle(mqttOutbound())
				.get();
	}

	@Bean
	public MessageChannel outChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("siSamplePublisher", mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("siSampleTopic");
		return messageHandler;
	}


	// 配置MessagingGateway
	@MessagingGateway(defaultRequestChannel = "outChannel")
	public interface Producer {
		void write(String note);
	}


}
