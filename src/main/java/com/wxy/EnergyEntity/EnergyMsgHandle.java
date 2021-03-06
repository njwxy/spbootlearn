package com.wxy.EnergyEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxy.comm.MyMessageHandler;
import com.wxy.comm.PvWebServerMessageHandle;
import com.wxy.ftm.SystemParams;
import com.wxy.gsonMessage.Result;
import com.wxy.pventity.PvNode;
import com.wxy.test.FrameData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import static com.wxy.test.PrjFuncs.Hex2Str;
import static com.wxy.test.PrjFuncs.getSendPacket;

@Component
public class EnergyMsgHandle  extends MyMessageHandler<DatagramPacket> {
    private final static Logger log = LoggerFactory.getLogger(EnergyMsgHandle.class);
    private final static short MS_SET_HEART_TIME = 0x23;
    private final static short MS_SET_HEART_TIME_ACK = 0xA3; //
    private final static short MS_HEART_REQ = 0xA5;   // node->MS
    private final static short MS_HEART_REQ_ACK = 0x25;
    private final static short MS_QUERY_ENG_DATA = 0x26;
    private final static short MS_QUERY_ENG_DATA_ACK = 0xA6;
    private final static short MS_HEART_READ_VER = 0x27;
    private final static short MS_HEART_READ_VER_ACK = 0xA7;
    private final static short MS_REMT_CTRL_RELAY = 0x28;
    private final static short MS_REMT_CTRL_RELAY_ACK = 0xA8;

    private ChannelHandlerContext localCtx=null;
    public Hashtable<Long,EnergyNode> nodeList;

    @Autowired
    EnergyMsgHandle energyMsgHandle;

    @Autowired
    ENodeService nodeService;

    @Autowired
    SystemParams systemParams;

    public EnergyMsgHandle() {
        nodeList = new Hashtable<Long,EnergyNode>(8192); /* max 4K items support now */
    }

    public  int sendSetRelayState(long devAddr,short relayState)
    {
        EnergyNode energyNode  =  nodeList.get(devAddr);
        if(energyNode==null) {
            log.error( "energy node" + devAddr + "not find");
            return 1;
        }
        else
        {
            FrameData frameHead = new FrameData(MS_REMT_CTRL_RELAY,devAddr);
            byte [] appData = new byte[2];
            appData[0] = 1; /*  collect module =1 communication module = 0  */
            appData[1] = (byte)relayState;
            byte[] sendPacket = getSendPacket(frameHead,appData,appData.length);
            log.info (Hex2Str(sendPacket,sendPacket.length));
            if(localCtx!=null)
                localCtx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket) ,energyNode.getClientIpAddr()));

        }
        return  0;
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msga) throws Exception {
        localCtx = ctx;

        final ByteBuf buf =  msga.content();
        byte [] msg= new byte[buf.readableBytes()];

        buf.readBytes(msg);
        FrameData frameData= new FrameData();
        frameData.getByteBuffer().put(msg,0,frameData.size());
        if (         ( frameData.start.get() != (byte) 0x68)
                || (msg[frameData.len.get()+9] != (byte) 0x16)
                )
        {
            log.error("wrong packet");
            return;
        }

        long devAddr = frameData.gwAddr.get();
        EnergyNode energyNode = nodeList.get(devAddr);

        if(energyNode== null)
        {
            // create node;
            energyNode = new EnergyNode(devAddr);
            nodeList.put(devAddr,energyNode);
            energyNode.keepLiveTime = 600;
        }

        energyNode.clientIpAddr = new InetSocketAddress(msga.sender().getAddress(),msga.sender().getPort());


        switch (frameData.ctrl.get())
        {
            case MS_HEART_REQ:
                {
                    FrameData frameHead = new FrameData(MS_HEART_REQ_ACK,devAddr);

                    byte[] sendPacket = getSendPacket(frameHead,null,0);
                    if(ctx!=null){
                        ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket),msga.sender()));
                    }

                }
                break;
            case MS_REMT_CTRL_RELAY_ACK: {
                int appPos = frameData.size();
                byte state = msg[appPos+1];
                EnergyRelayState energyRelayState = new EnergyRelayState(devAddr,state);
    /*           Gson gson = new GsonBuilder().setDateFormat("yy-MM-dd HH:mm:ss").create();
                Result<EnergyRelayState> result = new Result<EnergyRelayState>(EnergyRelayState.class.getSimpleName(),false,systemParams.getFmtId());
                result.data = energyRelayState;
                String jsonstr = gson.toJson(result);
             */
                String jsonstr = new Result<EnergyRelayState>(EnergyRelayState.class.getSimpleName(),false,systemParams.getFmtId(),energyRelayState).GetGsonString();
                log.info("recv CTRL_RELAY_ACK: "+ jsonstr);
                //System.out.println("send message to "+systemParams.getWebServerIp()+":"+systemParams.getWebServerPort());
                byte[] sendPacket = jsonstr.getBytes();
                DatagramPacket packet  = new DatagramPacket(Unpooled.wrappedBuffer(sendPacket),
                        new InetSocketAddress(systemParams.getEnergyWebServerIp(),systemParams.getEnergyWebServerPort()));
                if(energyMsgHandle.nioServer!=null)
                    energyMsgHandle.nioServer.SendPacket(packet);


                break;
            }

            case MS_QUERY_ENG_DATA_ACK:
                {
                    int appPos = frameData.size();
                    ENodeRptData nrd = new ENodeRptData();
                    int nodeRptDataLen = nrd.size();
                    nrd.getByteBuffer().put(msg,appPos,nodeRptDataLen);

                    energyNode.setValue(nrd.getVolt(0),nrd.getVolt(1),nrd.getVolt(2),
                            nrd.getCurrent(0),nrd.getCurrent(1),nrd.getCurrent(2),
                            nrd.getFreq(),
                            nrd.getPower(0),nrd.getPower(1),nrd.getPower(2),
                            nrd.getEnergy(0),nrd.getEnergy(1),nrd.getEnergy(2),nrd.getEnergy(3),
                            nrd.enodeState(),nrd.getRelayState(), new Date());
                    nodeService.createPvNode(energyNode);

                    Gson gson = new GsonBuilder().setDateFormat("yy-MM-dd HH:mm:ss").create();
                    //Gson gson = new GsonBuilder().create();

                    Result<EnergyNode> result = new Result<EnergyNode>(EnergyNode.class.getSimpleName(),false,systemParams.getFmtId());
                    result.data = energyNode;

                    String jsonstr = gson.toJson(result);
                    log.info(jsonstr);
                    //System.out.println("send message to "+systemParams.getWebServerIp()+":"+systemParams.getWebServerPort());
                    byte[] sendPacket = jsonstr.getBytes();

                    DatagramPacket packet  = new DatagramPacket(Unpooled.wrappedBuffer(sendPacket),
                            new InetSocketAddress(systemParams.getEnergyWebServerIp(),systemParams.getEnergyWebServerPort()));
                    if(energyMsgHandle.nioServer!=null)
                        energyMsgHandle.nioServer.SendPacket(packet);
                }
                break;
            default:
                break;
        }

        //gateWay.setClientIpAddr(new InetSocketAddress(msga.sender().getAddress(),msga.sender().getPort()));

    }
}
