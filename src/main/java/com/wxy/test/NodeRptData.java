package com.wxy.test;

import javolution.io.Struct;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Pattern;
 import java.util.regex.Matcher;

public class NodeRptData extends Struct {
/*
    u32 nodeAddr;
    u8 nodeState;
    u8 voltage[2];
    u8 temperature[3];
    u8 relayState;
    u8 signal[3];
    u8 time[6];
*/
    public final Unsigned32 nodeAddr = new Unsigned32();
    public final Unsigned8 nodeState = new Unsigned8();
    public final Unsigned8[] voltage = array(new Unsigned8[2]);
    public final Unsigned8[] temperature = array(new Unsigned8[3]);
    public final Unsigned8 relayState = new Unsigned8();
    public final Unsigned8[] signal = array(new Unsigned8[3]);
    public final Unsigned8[] time = array(new Unsigned8[6]);
   // public final Unsigned8[] dummy = array(new Unsigned8[5]);

    @Override
    public boolean isPacked() {
        return true;
    }

    @Override
    public ByteOrder byteOrder() {
        return ByteOrder.LITTLE_ENDIAN;
    }
    /*  bcd +/- 123.4 little endian
     *  */
    public int setTemperature(String setTemp){

      String patten = "^[-+]?([1-9]{1,3}|0)\\.\\d{1}$";
      Pattern r = Pattern.compile(patten);
      Matcher m = r.matcher(setTemp);
      if(m.find())
      {
          System.out.println(setTemp+" match");
          float fdata = Float.parseFloat(setTemp);
          System.out.println(fdata);

          if(fdata>0)
          {
              temperature[2].set((short)0);
          }
          else
          {
              temperature[2].set((short) 0x80);
              fdata = 0-fdata;
          }

          byte[] tempb = new byte[4];
          int dataout = (int) (fdata *10);
          System.out.println("dataout is "+dataout);
          tempb[3] = (byte) (dataout %10);
          tempb[2] = (byte) ((dataout/10)%10);
          tempb[1] = (byte) ((dataout/100) %10);
          tempb[0] = (byte) ((dataout/1000)%10);

          temperature[1].set( (short)(tempb[0]<<4 | tempb[1]));
          temperature[0].set( (short)(tempb[2]<<4 | tempb[3]));

          return 1;
      }


      System.out.println(setTemp+" not match");


      return 0;
    }

}
