package com.wxy.pventity;

import javolution.io.Struct;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


import  static com.wxy.test.PrjFuncs.hexa;

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

    //public Date dateTime;

    public  Date getTime(){
        Date dateTime = new Date(time[0].get()+100,time[1].get()-1,time[2].get(),time[3].get(),time[4].get(),time[5].get());
        return dateTime;
    }


    public int setTime(Date setDate ){

        short yy, mm, dd, hh, mi, ss;
        yy = (short) (setDate.getYear()-100);
        mm = (short)(setDate.getMonth()+1);
        dd = (short)setDate.getDate();

        hh = (short)(setDate.getHours());
        mi = (short)(setDate.getMinutes());
        ss = (short)(setDate.getSeconds());

        this.time[0].set(yy);
        this.time[1].set(mm);
        this.time[2].set(dd);
        this.time[3].set(hh);
        this.time[4].set(mi);
        this.time[5].set(ss);
        //setDateTime();
        return 0;
    }

    public void printNodeRpt(){
        Date dateTime = new Date(time[0].get()+100,time[1].get()-1,time[2].get(),time[3].get(),time[4].get(),time[5].get());
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ");
        System.out.println(sdf.format(dateTime)+"A:"+nodeAddr.get()+" S:"+nodeState.get()+" V:"+getVoltage()+
                " T:"+ getTemperatue() +" R:"+relayState.get()+" S:"+getSignal());

    }

    public float getTemperatue()
    {
        short [] btemp= new short[3];
        for(int i=0;i<3;i++)
            btemp[i] = temperature[i].get();
        float dataret = ((btemp[1]>>4))*1000 + (btemp[1]&0x0f)*100+(btemp[0]>>4)*10+(btemp[0]&0x0f) ;
        dataret = (btemp[2]==0x80)? -dataret:dataret;
        return dataret/10;
    }

    public float getVoltage()
    {
        short [] btemp= new short[2];
        for(int i=0;i<2;i++)
            btemp[i] = voltage[i].get();
        float dataret = ((btemp[1]>>4))*1000 + (btemp[1]&0x0f)*100+(btemp[0]>>4)*10+(btemp[0]&0x0f) ;
        return dataret/10;
    }

    public int getSignal()
    {
        short [] btemp= new short[3];
        for(int i=0;i<3;i++)
            btemp[i] = signal[i].get();
        int dataret = ((btemp[1]>>4))*1000 + (btemp[1]&0x0f)*100+(btemp[0]>>4)*10+(btemp[0]&0x0f) ;
        dataret = (btemp[2]==0x80)? -dataret:dataret;
        return dataret;
    }


    public NodeRptData() {

    }

    public NodeRptData(long nodeAddr, short nodeState, String voltage, String temperature, short relayState, String Signal) {
        this.nodeAddr.set(nodeAddr);
        this.nodeState.set(nodeState);
        setVoltage(voltage);
        setTemperature(temperature);
        this.relayState.set(relayState);
        setSignal(Signal);
        //dateTime = new Date(2019,3,19,11,20,30);
    }

    @Override
    public boolean isPacked() {
        return true;
    }

    @Override
    public ByteOrder byteOrder() {
        return ByteOrder.LITTLE_ENDIAN;
    }

    @Override
    public String toString() {
        int size = this.size();
        StringBuffer sb = new StringBuffer(size * 3);
        ByteBuffer buffer = this.getByteBuffer();
        int start = this.getByteBufferPosition();

        for(int i = 0; i < size; ++i) {
            int b = buffer.get(start + i) & 255;
            sb.append(hexa[b >> 4]);
            sb.append(hexa[b & 15]);
            sb.append((char)(','));
        }
        return sb.toString();
    }

    public int setVoltage(String setVoltage)
    {
        String patten = "^(([1-9]\\d{1,2}|0)\\.\\d{1}|0)$";
        Pattern r = Pattern.compile(patten);
        Matcher m = r.matcher(setVoltage);

        if(m.find()) {
            float fdata = Float.parseFloat(setVoltage);

            byte[] tempb = new byte[4];
            int dataout = (int) (fdata * 10);
            tempb[3] = (byte) (dataout % 10);
            tempb[2] = (byte) ((dataout / 10) % 10);
            tempb[1] = (byte) ((dataout / 100) % 10);
            tempb[0] = (byte) ((dataout / 1000) % 10);

            voltage[1].set((short) (tempb[0] << 4 | tempb[1]));
            voltage[0].set((short) (tempb[2] << 4 | tempb[3]));
            return 1;
        }
        System.out.println(setVoltage+" not match");
        return 0;
    }

    public int setSignal(String setSig){
        String patten = "^[+-]?(0|[1-9]\\d{0,2})$";
        Pattern r = Pattern.compile(patten);
        Matcher m = r.matcher(setSig);

        if(m.find())
        {
            //System.out.println(setSig+"  match");
            int fdata = Integer.parseInt(setSig);

            if(fdata>0)
            {
                signal[2].set((short)0);
            }
            else
            {
                signal[2].set((short) 0x80);
                fdata = 0-fdata;
            }

            byte[] tempb = new byte[4];

            tempb[3] = (byte) (fdata %10);
            tempb[2] = (byte) ((fdata/10)%10);
            tempb[1] = (byte) ((fdata/100) %10);
            tempb[0] = 0;

            signal[1].set( (short)(tempb[0]<<4 | tempb[1]));
            signal[0].set( (short)(tempb[2]<<4 | tempb[3]));
            return 1;
        }
        System.out.println(setSig+" not match");
        return 0;
    }


    /*  bcd +/- 123.4 little endian
     *  */
    public int setTemperature(String setTemp){

      String patten = "^[-+]?([1-9]{1,3}|0)\\.\\d{1}$";
      Pattern r = Pattern.compile(patten);
      Matcher m = r.matcher(setTemp);

      if(m.find())
      {
          float fdata = Float.parseFloat(setTemp);

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
