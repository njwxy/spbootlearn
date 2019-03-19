package com.wxy.test;
import javolution.io.Struct;

import java.nio.ByteBuffer;

public class TestJavolution  {

    public  static String Hex2Str(byte[] hexByteIn){
        int len = hexByteIn.length;
        String restult = new String();
        for(int i =0;i<len;i++)
        {
            restult += String.format( "%02x ",hexByteIn[i] );
        }
        return restult;
    }

    public static void main(String args[])
    {
        NodeRptData nodeRptData = new NodeRptData();
        nodeRptData.nodeAddr.set(0x1);
        nodeRptData.nodeState.set((short) 0x1);
        nodeRptData.voltage[0].set((short) 0x10);
        nodeRptData.voltage[1].set((short) 18);
        nodeRptData.temperature[0].set((short) 0x05);
        nodeRptData.temperature[1].set((short) 0x06);
        nodeRptData.temperature[2].set((short) 0x07);
        nodeRptData.relayState.set((short) 1);
        nodeRptData.signal[0].set((short) 0x80);
        nodeRptData.signal[1].set((short) 0x10);
        nodeRptData.signal[2].set((short) 0x11);
        for(short i=0x19;i<0x19+6;i++)
        {
            nodeRptData.time[i-0x19].set(i);
        }

        byte [] dataout = new byte[20];
       // nodeRptData.getByteBuffer().get(dataout,0,20);
       // System.out.println(dataout.length);
       // System.out.println(Hex2Str(dataout));
        ByteBuffer nodeRptDataByteBuffer = nodeRptData.getByteBuffer();

        nodeRptData.setTemperature("-112.3");
        nodeRptDataByteBuffer.position(0);
        nodeRptDataByteBuffer.get(dataout,0,20);
        System.out.println(Hex2Str(dataout));

        nodeRptData.setTemperature("112.35");



        nodeRptData.setTemperature("-995.3");
        nodeRptDataByteBuffer.position(0);
        nodeRptDataByteBuffer.get(dataout,0,20);
        System.out.println(Hex2Str(dataout));
        nodeRptData.setTemperature("-2.3");
        nodeRptDataByteBuffer.position(0);
        nodeRptDataByteBuffer.get(dataout,0,20);
        System.out.println(Hex2Str(dataout));

        nodeRptData.setTemperature("-0.3");
        nodeRptDataByteBuffer.position(0);
        nodeRptDataByteBuffer.get(dataout,0,20);
        System.out.println(Hex2Str(dataout));



    }
}
