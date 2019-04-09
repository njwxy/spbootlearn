package com.wxy.comm;

import com.wxy.testonly.TestMessageHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NIOServer {
    private final static Logger log = LoggerFactory.getLogger(NIOServer.class);

    private final NioEventLoopGroup acceptGroup;
    private final Bootstrap bootstrap ;
    private  Channel channel;
    private  int port;
    public MessageHandler messageHandler = null;


    public NIOServer(MessageHandler messageHandler,int port) {
        this.messageHandler = messageHandler;
        this.port = port;
        acceptGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(acceptGroup)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ServerByteHandler(messageHandler));
                        }
                    });


            try {
                channel = bootstrap.bind("0.0.0.0",port).sync().channel();
                log.info ("UdpServer start success" + port);
                channel.closeFuture().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                acceptGroup.shutdownGracefully();
            };
    }


    public static void main(String args[]){
        NIOServer nioServer = new NIOServer(new TestMessageHandle(),12345);
    }
}
