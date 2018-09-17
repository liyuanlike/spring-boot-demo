package com.github;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Mqtt {

    private String broker = "tcp://182.151.25.46:1883";
    private int qos = 1;
    private String username = "admin";
    private String password = "admin";
    private String serialNumber = "00:00:00:00";
    private String clientId = "GID_reader@ClientID_" + serialNumber;
    private String topic = "reader_push/" + serialNumber;
    private MqttClient sampleClient;

    public Mqtt() {
    }

    public static void main(String[] args) {
        Mqtt mqtt = new Mqtt();
        mqtt.connect();

    }


    public void connect() {
        if (this.sampleClient == null) {
            try {
                this.sampleClient = new MqttClient(broker, this.clientId, new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setUserName(username);
                connOpts.setServerURIs(new String[]{broker});
                connOpts.setPassword(password.toCharArray());
                connOpts.setCleanSession(false);
                connOpts.setKeepAliveInterval(60);
                connOpts.setAutomaticReconnect(true);
                this.sampleClient.setCallback(new MqttCallbackExtended() {
                    public void connectComplete(boolean reconnect, String serverURI) {
                        try {
                            Mqtt.this.sampleClient.subscribe(Mqtt.this.topic, Mqtt.this.qos);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        System.err.println("connect success");
                    }

                    public void connectionLost(Throwable throwable) {
                        System.err.println("mqtt connection lost");
                    }

                    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                        String str = new String(mqttMessage.getPayload(), "utf-8");
                        System.err.println("messageArrived:" + topic + "------" + str);
                    }

                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                        System.err.println("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
                    }
                });
                this.sampleClient.connect(connOpts);
            } catch (Exception me) {
                me.printStackTrace();
            }
        }
    }

    public void close() {
        if (this.sampleClient != null) {
            try {
                this.sampleClient.disconnect();
                this.sampleClient.close();
                this.sampleClient = null;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        if (this.sampleClient != null) {
            return this.sampleClient.isConnected();
        }
        return false;
    }
}
