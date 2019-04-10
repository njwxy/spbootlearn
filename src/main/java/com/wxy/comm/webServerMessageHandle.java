package com.wxy.comm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wxy.gsonMessage.Result;
import com.wxy.test.GateWay;
import com.wxy.test.PvMsgHandle;
import com.wxy.test.PvNode;
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

        //String  rawDataString = packet.getData().toString();
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
    }
}
