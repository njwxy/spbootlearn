package com.wxy.comm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface MessageHandler {
    //void setMainStation(MainStation ms);
    void protocolProcess(ChannelHandlerContext ctx, DatagramPacket msg) throws IOException;

}
