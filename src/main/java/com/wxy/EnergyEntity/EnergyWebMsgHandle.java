package com.wxy.EnergyEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wxy.comm.MyMessageHandler;
import com.wxy.comm.PvMsgHandle;
import com.wxy.comm.PvWebServerMessageHandle;
import com.wxy.gsonMessage.Result;
import com.wxy.pventity.GateWay;
import com.wxy.pventity.PvNode;
import com.wxy.pventity.RelayState;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
@Component
public class EnergyWebMsgHandle extends MyMessageHandler<DatagramPacket> {


    @Autowired
    EnergyMsgHandle energyMsgHandle;

    private ChannelHandlerContext localCtx=null;

    public EnergyWebMsgHandle(EnergyMsgHandle energyMsgHandle) {
        this.energyMsgHandle = energyMsgHandle;
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


        Gson gson = new GsonBuilder().create();

        if(retype.equals("energyRelay"))
        {
                Type userListType = new TypeToken<Result<EnergyRelayState>>(){}.getType();
                Result<EnergyRelayState> userListResult = gson.fromJson(msg,userListType);
                EnergyRelayState energyRelayState = userListResult.data;
                energyMsgHandle.sendSetRelayState(energyRelayState.devAddr,energyRelayState.state);
                log.info("setting "+energyRelayState.devAddr+"  state "+ energyRelayState.state);
        }
        else{
            log.error("error json message type");
        }

    }
}
