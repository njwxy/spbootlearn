package com.wxy.comm;

import io.netty.channel.ChannelHandlerContext;

public interface MessageHandler {
    //void setMainStation(MainStation ms);
    void protocolProcess(ChannelHandlerContext ctx, byte[] msg);
}
