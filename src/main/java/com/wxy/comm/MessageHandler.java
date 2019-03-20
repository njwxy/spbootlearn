package com.wxy.comm;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

@Service
public interface MessageHandler {
    //void setMainStation(MainStation ms);
    void protocolProcess(ChannelHandlerContext ctx, byte[] msg);
}
