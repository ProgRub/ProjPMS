package com.example.seaker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MQTTHelper {

    private String clientId = MqttClient.generateClientId();
    private MqttAndroidClient client;
    MqttConnectOptions mqttConnectOptions;

    public MQTTHelper(Context context, View v) throws MqttException {
        client = new MqttAndroidClient(context,
                "tcp://broker.hivemq.com:1883",
                clientId);

        try {
            IMqttToken token = client.connect();
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
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
                    // Something went wrong e.g. connection timeout or firewall
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(Context context, String jsonAsString){
        try{
            client.publish("Projeto/PMS/Grupo1/Grupo4", jsonAsString.getBytes(),0,false);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void subscribe(Context context, View view){
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //just for debug
                Toast.makeText(context,"You lost the connection with the topic: " + "Projeto/PMS/Grupo1/Grupo4" + ". Please connect again.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage jsonAsString) throws Exception {

                //AQUI RECEBEM A MENSAGEM jsonAsString (UMA STRING EM FORMATO JSON) - COM OS DADOS QUE NÓS ENVIAMOS:


                Log.d("Message:", jsonAsString + " from " + topic);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //empty
            }
        });
    }

}
