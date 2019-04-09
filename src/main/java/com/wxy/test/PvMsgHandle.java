package com.wxy.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxy.comm.MessageHandler;
import com.wxy.comm.NIOServer;

import com.wxy.testneo4j.Device;
import com.wxy.testneo4j.DeviceService;
import com.wxy.testneo4j.GwConfig;
import com.wxy.testneo4j.GwConfigReporsitory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;
import static com.wxy.test.PrjFuncs.*;

@Component
public class PvMsgHandle implements MessageHandler {
    private final static Logger log = LoggerFactory.getLogger(PvMsgHandle.class);
    private final static short MS_RNODE_DATA = 0x11;
    private final static short MS_RNODE_ACK = 0x91;
    private final static short MS_SET_RELAY = 0x12;
    private final static short MS_RELAY_ACK = 0x92;
    private final static short MS_RPT_DATA = 0x93;
    private final static short MS_HEART_REQ = 0x94;
    private final static short MS_HEART_ACK = 0x14;

    public ArrayList<GateWay> gateWayArrayList;
    private ChannelHandlerContext localCtx=null;

    public PvMsgHandle() {
        gateWayArrayList = new ArrayList<GateWay>();
    }

    private GateWay findGateWay(long gwAddr){
        for (GateWay gw: gateWayArrayList
             ) {
            if(gw.devAddr == gwAddr)
            {
                return gw;
            }
        }
        return  null;
    }

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private GwConfigReporsitory gwConfigReporsitory;

    @Autowired
    PvNodeService pvNodeService;

    @Autowired
    SystemParams systemParams;

   // public void setDeviceService(DeviceService deviceService) {
    //    this.deviceService = deviceService;
    //}


    public GateWay getGateWay(long gwAddr){
        GateWay gateWay = findGateWay(gwAddr);
        if(gateWay == null) {
            gateWay = new GateWay();

            /*网关配置信息*/
            //GwConfig gwConfig = gwConfigReporsitory.findByGwaddr(gwAddr);
            //if(gwConfig ==null)
            //{
               // log.error("gwaddr "+gwAddr+" is not configured");
               // return null;
            //}
            gateWay.devAddr = gwAddr;
            gateWay.heartInterval = systemParams.getHeartInterval();
            gateWay.pollingInterval = systemParams.getPollingInterval();
            gateWay.nodeList.clear();


            /*查找数据库*/
            List <Device> lstDevice = deviceService.getDevices(gwAddr);
            for(Device dev:lstDevice){
                long nodeAddr = dev.getDevAddr();
                gateWay.nodeList.put(nodeAddr,new PvNode(nodeAddr));
            }
            gateWayArrayList.add(gateWay);
        }
        return gateWay;
    }

    public  String sendSetRelayState(long devAddr,short relayState)
    {

        Device devGw  =  deviceService.findGwByNodeAddr(devAddr);
        if(devGw==null) {
            return "gateway of node" + devAddr + "not find";
        }

        else //(devGw!=null)
        {
            GateWay gateWay = getGateWay(devGw.getDevAddr());
            if(gateWay==null)
            {
                return "gateway of node " + devAddr + "not register";
            }
            else
            {
                PvNode pvNode = gateWay.nodeList.get(devAddr);
                if(pvNode!=null)
                {
                    //   byte[] sendPacket = getSendPacket(frameHead,appData,appData.length);
                    // nodeAddr+relaystate
                    FrameData frameHead = new FrameData(MS_SET_RELAY,gateWay.devAddr);
                    SetRelay setRelay = new SetRelay();
                    setRelay.nodeAddr.set(devAddr);
                    setRelay.relayState.set(relayState);
                    byte [] appData = new byte[setRelay.size()];
                    setRelay.getByteBuffer().get(appData);
                    byte[] sendPacket = getSendPacket(frameHead,appData,appData.length);
                    log.info (Hex2Str(sendPacket,sendPacket.length));
                    if(localCtx!=null)
                        localCtx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket) ,gateWay.getClientIpAddr()));
                }
                else
                {
                    return "node: "+devAddr+ " in "+ gateWay.devAddr + "not found";
                }
            }
        }
         return  "set relay ok";
    }


    @Override
    public void protocolProcess(ChannelHandlerContext ctx, DatagramPacket msga) {
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

        byte sum = getSum(msg,0,frameData.len.get()+8);
        if(sum != msg[frameData.len.get()+8])
        {
            log.error("checksum error");
            return;
        }


        long gwAddr = frameData.gwAddr.get();
        GateWay gateWay = getGateWay(gwAddr);
        if(gateWay== null)
            return;

        gateWay.setClientIpAddr(new InetSocketAddress(msga.sender().getAddress(),msga.sender().getPort()));



        switch (frameData.ctrl.get())
        {
            case MS_HEART_REQ:
            {
                byte needRpt= msg[frameData.size()];
                FrameData frameHead = new FrameData(MS_HEART_ACK,gwAddr);
                if(needRpt == 1)
                {
                    short nodeNum = (short)gateWay.nodeList.size();
                    if(nodeNum<100 && nodeNum>0){

                        HeartAck heartAck = new HeartAck((short)gateWay.heartInterval,(short)gateWay.pollingInterval);
                        heartAck.nodeNum.set(nodeNum);
                        //List <Long> listlong = new ArrayList<Long>();
                        //listlong = lstDevice.stream().map(e->{return  e.getDevAddr();}).collect(Collectors.toList());
                        int pos=0;
                        for(Map.Entry<Long,PvNode> entry:gateWay.nodeList.entrySet() ){
                            long nodeAddr = entry.getKey();
                            heartAck.nodeAddr[pos++].set(nodeAddr);
                        }

                        int datalen = heartAck.getPacketLength();
                        byte [] appData = new byte[datalen];
                        heartAck.getByteBuffer().get(appData);
                        byte[] sendPacket= getSendPacket(frameHead,appData,datalen);
                        if(ctx!=null){
                            System.out.println( Hex2Str(sendPacket,sendPacket.length));
                            ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket) ,gateWay.getClientIpAddr()));
                        }
                    }
                }

                if(needRpt ==0){
                    /* time6, heartInterval 1 pollingInterval;
                    * */
                    byte [] appData = new byte[8];
                    byte[] timenow = getTimeByteArray(new Date());
                    for(int i=0;i<6;i++)
                    {
                        appData[i] = timenow[i];
                    }
                    appData[6] = (byte) gateWay.heartInterval;
                    appData[7] =(byte) gateWay.pollingInterval;
                    byte[] sendPacket = getSendPacket(frameHead,appData,appData.length);
                    if(ctx!=null){
                        ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket),msga.sender()));
                    }
                }
            }
            break;
            case MS_RNODE_ACK:
            case MS_RELAY_ACK:{
                NodeRptData nodeRptData = new NodeRptData();
                nodeRptData.getByteBuffer().put(msg,frameData.size(),nodeRptData.size());
                PvNode pvNode = gateWay.nodeList.get(nodeRptData.nodeAddr.get());
                if(pvNode!=null)
                {
                    pvNode.temperature = nodeRptData.getTemperatue();
                    pvNode.relaySate = (byte)nodeRptData.relayState.get();
                    pvNode.voltage = nodeRptData.getVoltage();
                    pvNode.signal = nodeRptData.getSignal();
                    pvNode.time = nodeRptData.getTime();
                    log.info(pvNode.toString());
                }

            }
            break;

            case MS_RPT_DATA:{
                /*num+ num*NodeRptData*/
                int appPos = frameData.size();
                short itemnum = msg[appPos];
                //log.info(Hex2Str(msg,msg.length));
                ArrayList<PvNode> pvNodes = new ArrayList<PvNode>();
                for(int i=0;i<itemnum;i++)
                {
                    NodeRptData nodeRptData = new NodeRptData();
                    int nodeRptDataLen = nodeRptData.size();
                    nodeRptData.getByteBuffer().put(msg,appPos+1+i*nodeRptDataLen,nodeRptDataLen);
                   // log.info(nodeRptData.toString());
                    PvNode pvNode = gateWay.nodeList.get(nodeRptData.nodeAddr.get());
                    pvNode.temperature = nodeRptData.getTemperatue();
                    pvNode.relaySate = (byte)nodeRptData.relayState.get();
                    pvNode.voltage = nodeRptData.getVoltage();
                    pvNode.signal = nodeRptData.getSignal();
                    pvNode.time = nodeRptData.getTime();
                    pvNodes.add(pvNode);
                    pvNodeService.createPvNode(pvNode);
                    //String listStr = gson.toJson(devices);

                    //nodeRptData.printNodeRpt();

                }
                if(itemnum>0) {
                    Gson gson = new GsonBuilder().setDateFormat("yy-MM-dd HH:mm:ss").create();
                    //Gson gson = new GsonBuilder().create();
                    String jsonstr = gson.toJson(pvNodes);
                    log.info(jsonstr);


                    //System.out.println("send message to "+systemParams.getWebServerIp()+":"+systemParams.getWebServerPort());

                    byte[] sendPacket = jsonstr.getBytes();
                    //InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",12333);
                    ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket),new InetSocketAddress(systemParams.getWebServerIp(),systemParams.getWebServerPort())));
                   // PvNode[] array = new Gson().fromJson(jsonstr,PvNode[].class);
                   // List<PvNode> list = Arrays.asList(array);
                    //list.stream().forEach(pvNode ->{
                     //   System.out.println(pvNode.toString());
                    //});






                }
            }
            break;

            default:
                break;

        }




    }
}
