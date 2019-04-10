package com.wxy.comm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wxy.gsonMessage.Result;
import com.wxy.test.PvMsgHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class webServerMessageHandle implements UdpMessageCallback {
    private final static Logger log = LoggerFactory.getLogger(webServerMessageHandle.class);
    PvMsgHandle pvMsgHandle;

    public webServerMessageHandle(PvMsgHandle pvMsgHandle) {
        this.pvMsgHandle = pvMsgHandle;
    }

    @Override
    public void messageHandle(DatagramSocket socket, DatagramPacket packet) throws IOException {
        SocketAddress ftmAddress = packet.getSocketAddress();


        String msg = new String(packet.getData(), 0, packet.getLength());
        System.out.println("receive msg:" + msg);

        String  rawDataString = packet.getData().toString();
        JsonElement je = new JsonParser().parse(rawDataString);
        String retype  = je.getAsJsonObject().get("type").getAsString();
        boolean isList = je.getAsJsonObject().get("isList").getAsBoolean();
        long ftmId = je.getAsJsonObject().get("ftmId").getAsLong();


        if(retype.equals("getNodeAddrAck"))
        {
            if(isList==true)
            {
                Gson gson = new GsonBuilder().create();
                Type userListType = new TypeToken<Result<List<Long>>>(){}.getType();
                Result<List<Long>> userListResult = gson.fromJson(rawDataString,userListType);
                List<Long> nodeList = userListResult.data;
                System.out.println();
                System.out.println();
                nodeList.stream().forEach(node->System.out.print(node.toString()+" "));
                System.out.println();
            }
            else
            {
                log.error("getNodeAddrAck must be list format.");
            }

        }
    }
}
