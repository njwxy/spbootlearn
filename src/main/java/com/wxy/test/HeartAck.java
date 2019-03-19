package com.wxy.test;

import javolution.io.Struct;

import java.nio.ByteOrder;

public class HeartAck extends Struct {
    public final Unsigned8[] time = array(new Unsigned8[6]);
    public final Unsigned8 heartInterval = new Unsigned8();
    public final Unsigned8 pollingInterval = new Unsigned8();
    public final Unsigned8 nodeNum = new Unsigned8();
    public final Unsigned32[] nodeAddr = array(new Unsigned32[100]); //max 100

    @Override
    public boolean isPacked() {
        return true;
    }

    @Override
    public ByteOrder byteOrder() {
        return ByteOrder.LITTLE_ENDIAN;
    }

}
