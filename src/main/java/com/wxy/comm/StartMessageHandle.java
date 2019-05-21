package com.wxy.comm;

import com.wxy.EnergyEntity.EnergyMsgHandle;
import com.wxy.EnergyEntity.EnergyWebMsgHandle;
import com.wxy.ftm.ApplicationContextProvider;
import com.wxy.ftm.SystemParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
@Slf4j
public class StartMessageHandle {
    @Autowired
    ApplicationContextProvider applicationContextProvider;

    @Autowired
    PvMsgHandle pvMsgHandle;
    @Autowired
    EnergyMsgHandle energyMsgHandle;

    @Autowired
    EnergyWebMsgHandle energyWebMsgHandle;

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
        startNioFunc(energyMsgHandle,systemParams.getEnergyFrontServerPort());  // 前置机端口
        startNioFunc(energyWebMsgHandle,systemParams.getEnergyWebListenPort()); // 监听web服务器下发的json数据包
    }


}
