package com.wxy.comm;

import com.wxy.testonly.TestMessageHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("nioserver")
@Slf4j
@Scope("prototype")

public class NIOServer extends Thread{

    private NioEventLoopGroup acceptGroup;
    private Bootstrap bootstrap ;
    private  Channel channel=null;

    @Setter
    @Getter
    private int port;

    @Setter
    @Getter
    MyMessageHandler messageHandler;

    public void SendPacket(DatagramPacket packet)
    {
        if(channel!=null)
            channel.writeAndFlush(packet);
    }

    public void initNioServer() {
        acceptGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(acceptGroup)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(messageHandler);
                        }
                    });
    }

    @Override
    public void run()
    {
        try {
            channel = bootstrap.bind("0.0.0.0",port).sync().channel();
            log.info ("UdpServer start at " + port);
            channel.closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            acceptGroup.shutdownGracefully();
        };
    }
}
