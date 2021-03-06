package com.wxy.comm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.wxy.gsonMessage.Result;
import com.wxy.mqtt.MqttProducer;
import com.wxy.pventity.*;
import com.wxy.test.FrameData;
import com.wxy.ftm.SystemParams;
import com.wxy.simuGraphDb.DeviceService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

import static com.wxy.test.PrjFuncs.*;
import static com.wxy.test.PrjFuncs.Hex2Str;

@Component
public class PvMsgHandle extends MyMessageHandler<DatagramPacket> {
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
   // private  UdpServer udpServer;
    //private WebServerMessageHandle webServerMessageHandle=null;
    //private NIOServer webNioServer;
    @Autowired
    PvWebServerMessageHandle pvWebServerMessageHandle;

    @Autowired
    MqttProducer mqttProducer;

    @Autowired
    private DeviceService deviceService;
   // @Autowired
   // private GwConfigReporsitory gwConfigReporsitory;

    @Autowired
    PvNodeService pvNodeService;

    @Autowired
    SystemParams systemParams;

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

            gateWayArrayList.add(gateWay);

            /*查找数据库*/
           /* List <Device> lstDevice = deviceService.getDevices(gwAddr);
            for(Device dev:lstDevice){
                long nodeAddr = dev.getDevAddr();
                gateWay.nodeList.put(nodeAddr,new PvNode(nodeAddr));
            }
            gateWayArrayList.add(gateWay);*/
        }
        return gateWay;
    }

    public  String sendSetRelayState(RelayState rs)
    {
       // long gwAddr, long devAddr,short relayState
        //Device devGw  =  deviceService.findGwByNodeAddr(devAddr);
        //if(devGw==null) {
//            return "gateway of node" + devAddr + "not find";
  //      }

    //    else //(devGw!=null)

            GateWay gateWay = getGateWay(rs.gwAddr);
            if(gateWay==null)
            {
                return "gateway of node " + rs.devAddr + "not register";
            }
            else
            {
                PvNode pvNode = gateWay.nodeList.get(rs.devAddr);
                if(pvNode!=null)
                {
                    //   byte[] sendPacket = getSendPacket(frameHead,appData,appData.length);
                    // nodeAddr+relaystate
                    FrameData frameHead = new FrameData(MS_SET_RELAY,gateWay.devAddr);
                    SetRelay setRelay = new SetRelay();
                    setRelay.nodeAddr.set(rs.devAddr);
                    setRelay.relayState.set((short) rs.state);
                    byte [] appData = new byte[setRelay.size()];
                    setRelay.getByteBuffer().get(appData);
                    byte[] sendPacket = getSendPacket(frameHead,appData,appData.length);
                    log.info (Hex2Str(sendPacket,sendPacket.length));
                    if(localCtx!=null)
                        localCtx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket) ,gateWay.getClientIpAddr()));
                }
                else
                {
                    return "node: "+rs.devAddr+ " in "+ gateWay.devAddr + "not found";
                }
            }
         return  "set relay ok";
    }



    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msga) throws Exception {


        localCtx = ctx;
        final ByteBuf buf =  msga.content();
        byte [] msg= new byte[buf.readableBytes()];
        buf.readBytes(msg);

        System.out.println(Hex2Str(msg,msg.length));

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

        if(gateWay.nodeList.size()==0)
        {
            Result<Long> result = new Result<Long>("getNodeAddr", false, systemParams.getFmtId());
            result.data = new Long(gwAddr);
            Gson gson = new GsonBuilder().create();
            String outstr = gson.toJson(result);
            System.out.println(outstr);
            byte[] boutstr = outstr.getBytes();
          //   DatagramPacket packet = new DatagramPacket(boutstr, boutstr.length, ));
            DatagramPacket packet = new DatagramPacket(Unpooled.wrappedBuffer(boutstr) ,new InetSocketAddress(systemParams.getWebServerIp(), systemParams.getWebServerPort()));

            if(pvWebServerMessageHandle.nioServer!=null)
                pvWebServerMessageHandle.nioServer.SendPacket(packet);
            //webServerMessageHandle.sendPacket(packet);

            //udpServer.sendPacket(packet);
        }

        gateWay.setClientIpAddr(new InetSocketAddress(msga.sender().getAddress(),msga.sender().getPort()));

        switch (frameData.ctrl.get())
        {
            case MS_HEART_REQ:
            {
                log.info("recv MS_HEART_REQ:"+Hex2Str(msg,msg.length));
                byte needRpt= msg[frameData.size()];
                FrameData frameHead = new FrameData(MS_HEART_ACK,gwAddr);
                if(needRpt == 1)
                {
                    gateWay.pollingInterval = 0;

                    if(gateWay.nodeLoad==false) {
                        break;
                    }
                    short nodeNum = (short)gateWay.nodeList.size();
                    if(nodeNum<=250 && nodeNum>0){

                        HeartAck heartAck = new HeartAck((short)gateWay.heartInterval,(short)gateWay.pollingInterval);
                        heartAck.nodeNum.set(nodeNum);
                        //List <Long> listlong = new ArrayList<Long>();
                        //listlong = lstDevice.stream().map(e->{return  e.getDevAddr();}).collect(Collectors.toList());
                       // List<Long> listnode = new ArrayList<Long>();
                        int pos=0;

                        for(Map.Entry<Long,PvNode> entry:gateWay.nodeList.entrySet() ){
                            long nodeAddr = entry.getKey();
                            //listnode.add(nodeAddr);
                            pos++;
                            heartAck.nodeAddr[nodeNum-pos].set(nodeAddr);
                        }
                        //List<Long> listSorted = listnode.stream().sorted().collect(Collectors.toList());

                        /*pos =0;
                        for (long d:listSorted
                             ) {
                            heartAck.nodeAddr[pos++].set(d);
                        }*/

                        int datalen = heartAck.getPacketLength();
                        byte [] appData = new byte[datalen];
                        heartAck.getByteBuffer().get(appData);
                        byte[] sendPacket= getSendPacket(frameHead,appData,datalen);
                        if(ctx!=null){
                            log.info("send MS_HEART_ACK:"+Hex2Str(sendPacket,sendPacket.length));
                            ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket) ,gateWay.getClientIpAddr()));
                        }
                    }
                }

                if(needRpt ==0){
                    /* time6, heartInterval 1 pollingInterval;
                    * */
                    gateWay.pollingInterval = 0;

                    byte [] appData = new byte[9];
                    byte[] timenow = getTimeByteArray(new Date());
                    for(int i=0;i<6;i++)
                    {
                        appData[i] = timenow[i];
                    }
                    appData[6] = (byte) gateWay.heartInterval;
                    appData[7] =(byte) gateWay.pollingInterval;
                    appData[8] = (byte) 0;
                    byte[] sendPacket = getSendPacket(frameHead,appData,appData.length);
                    if(ctx!=null){
                        log.info("send MS_HEART_ACK:"+Hex2Str(sendPacket,sendPacket.length));
                        ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket),gateWay.getClientIpAddr()));
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
                    pvNode.relayState = (byte)nodeRptData.relayState.get();
                    pvNode.voltage = nodeRptData.getVoltage();
                    pvNode.signal = nodeRptData.getSignal();
                    pvNode.time = nodeRptData.getTime();
                    log.info(pvNode.toString());
                }

            }
            break;

            case MS_RPT_DATA:{
                /*num+ num*NodeRptData*/
                log.info("recv MS_RPT_DATA:"+Hex2Str(msg,msg.length));
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
                    if(pvNode==null)
                        continue;
                    pvNode.temperature = nodeRptData.getTemperatue();
                    pvNode.relayState = (byte)nodeRptData.relayState.get();
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

                    Result <List<PvNode>> result = new Result<List<PvNode>>(PvNode.class.getSimpleName(),true,1);
                    result.data = pvNodes;


                    String jsonstr = gson.toJson(result);
                    log.info(jsonstr);
                    //System.out.println("send message to "+systemParams.getWebServerIp()+":"+systemParams.getWebServerPort());
                    byte[] sendPacket = jsonstr.getBytes();

                    DatagramPacket packet  = new DatagramPacket(Unpooled.wrappedBuffer(sendPacket),
                            new InetSocketAddress(systemParams.getWebServerIp(),systemParams.getWebServerPort()));
                    if(pvWebServerMessageHandle.nioServer!=null)
                        pvWebServerMessageHandle.nioServer.SendPacket(packet);

                    if(mqttProducer !=null)
                        mqttProducer.sendMessage(jsonstr);
                }
            }
            break;

            default:
                break;

        }




    }
}
