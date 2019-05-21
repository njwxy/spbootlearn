package com.wxy.mqtt;

import com.wxy.ftm.ApplicationContextProvider;
import com.wxy.ftm.MosConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@IntegrationComponentScan
public class MqttProducer {

    @Autowired
    MosConfig mosConfig;

   // @Setter
   // @Getter
  //  private static ApplicationContext applicationContext;

    private MyGateway myGateway;

    //@Scheduled(cron = "*/5 * *  * * * ")
    public void sendMessage(String msg){
        //if(applicationContext==null)
         //   return;
       // MyGateway gateway = applicationContext.getBean(MyGateway.class);
      //  NIOServer nioServer = ApplicationContextProvider.getBean("nioserver",NIOServer.class);
        if(myGateway ==null)
            myGateway = ApplicationContextProvider.getBean(MyGateway.class);

        if(myGateway!=null)
            myGateway.sendToMqtt(msg);

    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setServerURIs(mosConfig.getUrl());
        log.info("mqtt server url:"+mosConfig.getUrl());


        // factory.setUserName("username");
        // factory.setPassword("password");
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler("testClient", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mosConfig.getTopic());
        log.info("mqtt server topic:"+mosConfig.getTopic());
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }


    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MyGateway {
        void sendToMqtt(String data);
    }
}
