package com.wxy.test;

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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    public PvMsgHandle() {
        gateWayArrayList = new ArrayList<GateWay>();
    }

    GateWay findGateWay(long gwAddr){
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

   // public void setDeviceService(DeviceService deviceService) {
    //    this.deviceService = deviceService;
    //}

    @Override
    public void protocolProcess(ChannelHandlerContext ctx, DatagramPacket msga) {
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

        switch (frameData.ctrl.get())
        {
            case MS_HEART_REQ:
            {

                byte needRpt= msg[frameData.size()];
                long gwAddr = frameData.gwAddr.get();

                GateWay gateWay = findGateWay(gwAddr);
                if(gateWay == null) {
                    gateWay = new GateWay();

                    /*网关配置信息*/
                    GwConfig gwConfig = gwConfigReporsitory.findByGwaddr(gwAddr);
                    if(gwConfig ==null)
                    {
                        log.error("gwaddr "+gwAddr+" is not configured");
                        break;
                    }
                    gateWay.devAddr = gwAddr;
                    gateWay.heartInterval = gwConfig.getHeartInterval();
                    gateWay.pollingInterval = gwConfig.getPollingInterval();
                    gateWay.nodeList.clear();


                    /*查找数据库*/
                    List <Device> lstDevice = deviceService.getDevices(gwAddr);
                    for(Device dev:lstDevice){
                        long nodeAddr = dev.getDevAddr();
                        gateWay.nodeList.put(nodeAddr,new PvNode(nodeAddr));
                    }
                    gateWayArrayList.add(gateWay);
                }

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
                            ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(sendPacket) ,msga.sender()));
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
            case MS_RELAY_ACK:{

            }
            break;

            case MS_RPT_DATA:{
                /*num+ num*NodeRptData*/
                int appPos = frameData.size();
                short itemnum = msg[appPos];
                log.info(Hex2Str(msg,msg.length));
                for(int i=0;i<itemnum;i++)
                {
                    NodeRptData nodeRptData = new NodeRptData();
                    int nodeRptDataLen = nodeRptData.size();
                    nodeRptData.getByteBuffer().put(msg,appPos+1+i*nodeRptDataLen,nodeRptDataLen);
                    log.info(nodeRptData.toString());
                    nodeRptData.printNodeRpt();
                }
            }
            break;

            default:
                break;

        }




    }
}
