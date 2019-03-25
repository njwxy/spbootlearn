package com.wxy.comm;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import static com.wxy.test.PrjFuncs.Hex2Str;

public class ServerByteHandler  extends SimpleChannelInboundHandler<DatagramPacket>  {
    private MessageHandler messageHandler=null;

    public ServerByteHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    private final static Logger log = LoggerFactory.getLogger(ServerByteHandler.class);



    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
 /*       final ByteBuf buf = msg.content();
        int readableBytes = buf.readableBytes();
        byte[] content = new byte[readableBytes];
        buf.readBytes(content);
        String reqmsg = Hex2Str(content,content.length);
        log.info(reqmsg);

   */
        if(messageHandler !=null) {
                messageHandler.protocolProcess(ctx, msg);
            }
        }
}

