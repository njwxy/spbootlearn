package com.wxy.test;

import com.wxy.comm.MessageHandler;
import com.wxy.comm.NIOServer;

import com.wxy.testneo4j.DeviceService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wxy.test.PrjFuncs.getSum;

@Component
public class PvMsgHandle implements MessageHandler {
    private final static Logger log = LoggerFactory.getLogger(PvMsgHandle.class);
    private final static short MS_RNODE_DATA = 0x11;
    private final static short MS_RNODE_ACK = 0x91;
    private final static short MS_SET_RELAY = 0x12;
    private final static short MS_RELAY_ACK = 0x92;
    private final static short MS_RPT_DATA = 0x93;
    private final static short MS_HEART_REQ = 0x94;
    private final static short MS_HEART_ACK = 0x14;

    @Autowired
    private DeviceService deviceService;
   // public void setDeviceService(DeviceService deviceService) {
    //    this.deviceService = deviceService;
    //}

    @Override
    public void protocolProcess(ChannelHandlerContext ctx, byte[] msg) {
        FrameData frameData= new FrameData();
        frameData.getByteBuffer().put(msg,0,frameData.size());
        if (         ( frameData.start.get() != (byte) 0x68)
                      || (msg[frameData.len.get()+9] != (byte) 0x16)
            )
        {
            log.error("wrong packet");
            return;
        }

        byte sum = getSum(msg,0,frameData.len.get()+8);
        if(sum != msg[frameData.len.get()+8])
        {
            log.error("checksum error");
            return;
        }

        switch (frameData.ctrl.get())
        {
            case 0x94:
                List lstDevice =  deviceService.getDevices(100);
                System.out.println(lstDevice);
                break;
        }




    }
}
