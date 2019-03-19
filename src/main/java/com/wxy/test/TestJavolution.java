package com.wxy.test;
import javolution.io.Struct;
import org.springframework.format.annotation.DateTimeFormat;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJavolution  {



    public static void main(String args[])
    {



        //NodeRptData nodeRptData = new NodeRptData(1,(short)1,"40.5","-25.3", (short) 1,"-123");
        //Date now = new Date(119,2,5,17,37,59);
        //nodeRptData.setTime(now);
        //System.out.println("T:"+ nodeRptData.getTemperatue() +" V:"+nodeRptData.getVoltage()+" S:"+nodeRptData.getSignal());
        //nodeRptData.printNodeRpt();
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println(sdf.format(now));
       // nodeRptData.setTime(now);
        byte[] testbytes = {0x68,0x05,0x00,0x00,0x00,0x01,0x05,0x04,0x64,0x02,  0x00,0x01,0x23,0x01,(byte)0x80,0x3B,0x25,0x11,0x05,0x03,0x14};
        NodeRptData testRpt = new NodeRptData();
        testRpt.getByteBuffer().put(testbytes,1,20);
        testRpt.printNodeRpt();


        System.out.println(testRpt);
    }
}
