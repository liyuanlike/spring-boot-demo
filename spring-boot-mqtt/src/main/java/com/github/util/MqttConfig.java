package com.github.util;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
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
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;


@Configuration
public class MqttConfig {

	private String clientIdConsumer = "spring-boot-mqtt-1";
	private String broker = "tcp://182.151.25.46:1883";
	private String userName = "admin";
	private String password = "admin";
	private String topic = "reader/asf";
	private String loginTopic = "reader/login/#";

	// 配置ClientFactory
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {

		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
		mqttConnectOptions.setServerURIs(new String[]{broker});
		mqttConnectOptions.setUserName(userName);
		mqttConnectOptions.setPassword(password.toCharArray());
		mqttConnectOptions.setCleanSession(false);
		mqttConnectOptions.setAutomaticReconnect(true);
        factory.setConnectionOptions(mqttConnectOptions);
		return factory;
	}


	// 配置consumer
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducerSupport mqttInbound(MqttPahoClientFactory mqttClientFactory) {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientIdConsumer, mqttClientFactory, loginTopic);
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				MessageHeaders messageHeaders = message.getHeaders();
				System.err.println(messageHeaders);
				System.err.println(message);
				System.err.println(message.getPayload() + ": consumer...");
			}
		};
	}


	// 配置producer
	/**/
	private String clientIdProducer = "spring-boot-mqtt-1";
	@Bean
	public MessageChannel outChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "outChannel")
	public MessageHandler mqttOutbound(MqttPahoClientFactory mqttClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientIdProducer, mqttClientFactory);
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(topic);
		return messageHandler;
	}

	// 配置MessagingGateway
	@MessagingGateway(defaultRequestChannel = "outChannel")
	public interface MqttGateway {
		void write(String note);
		void sendToMqtt(String data);
		void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);
		void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
	}


}

