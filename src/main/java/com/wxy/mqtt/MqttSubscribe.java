package com.wxy.mqtt;

import com.wxy.ftm.MosConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class MqttSubscribe {

    @Autowired
    MosConfig mosConfig;

    @Bean
    public MessageChannel mqttInputChannel() {
        MessageChannel messageChannel  =  new DirectChannel();
        ((DirectChannel) messageChannel).subscribe(mqttInput());
        return  messageChannel;
    }


    //@Bean
    // @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttInput() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message.getPayload());
            }
        };
    }
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mosConfig.getUrl(), "mossub",mosConfig.getTopic());
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        //adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }
}
