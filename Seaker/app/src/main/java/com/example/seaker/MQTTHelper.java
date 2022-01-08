package com.example.seaker;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTHelper {

    private String clientId = MqttClient.generateClientId();
    private MqttAndroidClient client;
    private boolean connected;
    private IMqttToken token;
    private MqttConnectOptions mqttConnectOptions;

    private static MQTTHelper instance=null;

    public static MQTTHelper getInstance(Context context) throws MqttException {
        if(instance==null) instance=new MQTTHelper(context);
        return instance;
    }

    public MQTTHelper(Context context) {
        connected = false;
        client = new MqttAndroidClient(context,
                "tcp://broker.hivemq.com:1883",
                clientId);

        try {
            tryConnect(context);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                connected = false;
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {}
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
    }

    public IMqttToken connectMQTT() throws MqttException {
        IMqttToken token = client.connect();
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        return token;
    }

    public void publish(Context context, String jsonAsString){
        try{
            client.publish("Projeto/PMS/Grupo1/Grupo4", jsonAsString.getBytes(),0,false);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void tryConnect(Context context) throws MqttException {
        token = connectMQTT();

        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                connected = true;
                // We are connected

                //Adjusting the set of options that govern the behaviour of Offline (or Disconnected) buffering of messages
                DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                disconnectedBufferOptions.setBufferEnabled(true);
                disconnectedBufferOptions.setBufferSize(100);
                disconnectedBufferOptions.setPersistBuffer(false);
                disconnectedBufferOptions.setDeleteOldestMessages(false);
                client.setBufferOpts(disconnectedBufferOptions);

            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                connected = false;
                // Something went wrong e.g. connection timeout or firewall
            }
        });
    }

    public boolean isConnected() {
        return connected;
    }

}
