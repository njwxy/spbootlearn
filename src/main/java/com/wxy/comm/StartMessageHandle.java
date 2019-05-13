package com.wxy.comm;

import com.wxy.EnergyEntity.EnergyMsgHandle;
import com.wxy.ftm.SystemParams;
import com.wxy.mqtt.MqttProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
@Slf4j
public class StartMessageHandle {
    @Autowired
    PvMsgHandle pvMsgHandle;
    @Autowired
    EnergyMsgHandle energyMsgHandle;
    @Autowired
    PvWebServerMessageHandle pvWebServerMessageHandle;
    @Autowired
    SystemParams systemParams;


    void startNioFunc(MyMessageHandler messageHandler,int port){
        NIOServer nioServer = ApplicationContextProvider.getBean("nioserver",NIOServer.class);
        messageHandler.setNioServer(nioServer);
        nioServer.setMessageHandler(messageHandler);
        nioServer.setPort(port);
        nioServer.initNioServer();
        nioServer.start();
    }

    @PostConstruct
    public  void  startNio(){
        startNioFunc(pvMsgHandle,systemParams.getFrontServerPort());
        startNioFunc(pvWebServerMessageHandle,systemParams.getWebListenPort());
    }


}
