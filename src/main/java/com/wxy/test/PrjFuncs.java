package com.wxy.test;

import java.nio.ByteBuffer;

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
}
