package com.wxy.test;

import java.nio.ByteBuffer;
import java.util.Date;

public class PrjFuncs {
    public static final char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public  static String Hex2Str(byte[] hexByteIn,int len1){
        int len = hexByteIn.length;
        if(len1<len)
            len = len1;

        StringBuffer sb = new StringBuffer(len * 3);

        for(int i = 0; i < len; ++i) {
            int b = hexByteIn[i]  & 255;
            sb.append(hexa[b >> 4]);
            sb.append(hexa[b & 15]);
            sb.append((char)(' '));
        }
        return sb.toString();
    }

    public static byte getSum(byte[] data,int start,int length)
    {
        int ret = 0;
        for(int i = 0;i<length;i++)
        {
            ret = ret + (int)(data[i+start]&0xff);
        }
        byte retb = (byte)((ret)&0xff);
        return retb;
    }

    public static byte[] getTimeByteArray(Date datatime)
    {
        byte [] retTime = new byte[6];
        short yy, mm, dd, hh, mi, ss;

        retTime[5]  = (byte)(datatime.getYear()-100);
        retTime[4]  = (byte)(datatime.getMonth()+1);
        retTime[3]  = (byte)datatime.getDate();

        retTime[2]  = (byte)(datatime.getHours());
        retTime[1]  = (byte)(datatime.getMinutes());
        retTime[0]  = (byte)(datatime.getSeconds());
        return retTime;
    }

    public static byte[] getSendPacket(FrameData frameHead, byte[] appData, int datalen)
    {
        frameHead.len.set(datalen);
        byte[] packet = new byte[frameHead.size()+datalen+2];
        ByteBuffer byteBuffer= frameHead.getByteBuffer();
        byteBuffer.get(packet,0,frameHead.size());

        if(datalen>0) {
            System.arraycopy(appData, 0, packet, frameHead.size(), datalen);
        }

        int pos = frameHead.size()+datalen;
        packet[pos++] = getSum(packet,0,pos);
        packet[pos] = 0x16;
        return packet;
    }



}
