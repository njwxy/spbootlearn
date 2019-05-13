package com.wxy.comm;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wxy.gsonMessage.Result;
import com.wxy.pventity.GateWay;
import com.wxy.pventity.PvNode;
import com.wxy.pventity.RelayState;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
@Component
public class PvWebServerMessageHandle extends MyMessageHandler<DatagramPacket> {

    private PvMsgHandle pvMsgHandle=null;
    private ChannelHandlerContext localCtx=null;

    public PvWebServerMessageHandle(PvMsgHandle pvMsgHandle) {
        this.pvMsgHandle = pvMsgHandle;
    }
    public void sendPacket(DatagramPacket packet)
    {
        if(localCtx!=null)
        {
            localCtx.writeAndFlush(packet);
        }

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msga) throws Exception {

        localCtx = ctx;
        final ByteBuf buf =  msga.content();
        byte [] msgb= new byte[buf.readableBytes()];
        buf.readBytes(msgb);

        String msg = new String(msgb);
        System.out.println("receive msg:" + msg);
        InetSocketAddress clientaddr = msga.sender();
        JsonElement je = new JsonParser().parse(msg);
        String retype  = je.getAsJsonObject().get("type").getAsString();
        boolean isList = je.getAsJsonObject().get("isList").getAsBoolean();
        long ftmId = je.getAsJsonObject().get("ftmId").getAsLong();


        if(retype.equals("getNodeAddrAck"))
        {
            if(isList==true)
            {
                Gson gson = new GsonBuilder().create();
                Type userListType = new TypeToken<Result<List<Long>>>(){}.getType();
                Result<List<Long>> userListResult = gson.fromJson(msg,userListType);
                List<Long> nodeList = userListResult.data;
                int nodenum = nodeList.size()-1;
                GateWay gateWay =  pvMsgHandle.getGateWay(nodeList.get(0));
                if(gateWay !=null)
                {
                    if(gateWay.nodeList.size()>0)
                        gateWay.nodeList.clear();

                    for(int i=1;i<nodeList.size();i++)
                    {
                        long nodeAddr = nodeList.get(i);
                        gateWay.nodeList.put(nodeAddr,new PvNode(nodeAddr));
                    }
                    gateWay.nodeLoad = true;
                }

                nodeList.stream().forEach(node->System.out.print(node.toString()+" "));
                System.out.println();
            }
            else
            {
                log.error("getNodeAddrAck must be list format.");
            }
        }
        else if(retype.equals("RelayState"))
        {
            Gson gson = new GsonBuilder().create();
            Type userListType = new TypeToken<Result<RelayState>>(){}.getType();
            Result<RelayState> relayStateResult = gson.fromJson(msg,userListType);
            RelayState relayState = relayStateResult.data;
            pvMsgHandle.sendSetRelayState(relayState);
        }

    }
}
