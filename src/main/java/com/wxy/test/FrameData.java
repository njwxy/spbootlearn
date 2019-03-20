package com.wxy.test;

import javolution.io.Struct;

import java.nio.ByteOrder;

public class FrameData extends Struct {
    /*
    *
    * */
    public final Unsigned8 start = new Unsigned8();
    public final Unsigned8 ctrl = new Unsigned8();
    public final Unsigned32 gwAddr = new Unsigned32();
    public final Unsigned16 len = new Unsigned16();

    public FrameData(short ctrl,long gwAddr) {
        this.start.set((short) 0x68);
        this.ctrl.set(ctrl);
        this.gwAddr.set(gwAddr);
    }

    public FrameData() {
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
