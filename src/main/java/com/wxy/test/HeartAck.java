package com.wxy.test;

import javolution.io.Struct;

import java.nio.ByteOrder;
import java.util.Date;

import static com.wxy.test.PrjFuncs.getTimeByteArray;

public class HeartAck extends Struct {
    /*
    *   len = 9+n*nodenum
    * */
    public final Unsigned8[] time = array(new Unsigned8[6]);
    public final Unsigned8 heartInterval = new Unsigned8();
    public final Unsigned8 pollingInterval = new Unsigned8();
    public final Unsigned8 nodeNum = new Unsigned8();
    public final Unsigned32[] nodeAddr = array(new Unsigned32[100]); //max 100

    public int getPacketLength()
    {
        return 9 + nodeNum.get()*4;
    }


    public HeartAck(short heartInterval,short pollingInterVal) {
        Date datatime = new Date();
        byte [] timeget = getTimeByteArray(datatime);
        for(int i=0;i<6;i++)
            time[i].set(timeget[i]);
        this.heartInterval.set( heartInterval);
        this.pollingInterval.set(pollingInterVal);
    }

    @Override
    public boolean isPacked() {
        return true;
    }

    @Override
    public ByteOrder byteOrder() {
        return ByteOrder.LITTLE_ENDIAN;
    }

}
