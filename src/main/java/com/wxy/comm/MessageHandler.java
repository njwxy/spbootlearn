package com.wxy.comm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.stereotype.Service;

@Service
public interface MessageHandler {
    //void setMainStation(MainStation ms);
    void protocolProcess(ChannelHandlerContext ctx, DatagramPacket msg);

}
