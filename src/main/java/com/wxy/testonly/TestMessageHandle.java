package com.wxy.testonly;

import com.wxy.comm.MessageHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.wxy.test.PrjFuncs.Hex2Str;

public class TestMessageHandle implements MessageHandler {
    private final static Logger log = LoggerFactory.getLogger(TestMessageHandle.class);
    @Override
    public void protocolProcess(ChannelHandlerContext ctx, DatagramPacket msg)
    {

        final ByteBuf buf = msg.content();
        int readableBytes = buf.readableBytes();
        byte[] content = new byte[readableBytes];
        buf.readBytes(content);
        String reqmsg = Hex2Str(content,content.length);
        log.info(reqmsg);
        ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer("helloClient".getBytes()),msg.sender()));
    }
}
