package com.wxy.comm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyMessageHandler<DatagramPacket> extends SimpleChannelInboundHandler<DatagramPacket> {
    public NIOServer nioServer;
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

    }
}
