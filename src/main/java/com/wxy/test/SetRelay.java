package com.wxy.test;

import javolution.io.Struct;

import java.nio.ByteOrder;

public class SetRelay extends Struct {
        public final Unsigned32 nodeAddr = new Unsigned32();
        public final Unsigned8 relayState = new Unsigned8();
        @Override
        public boolean isPacked() {
            return true;
        }

        @Override
        public ByteOrder byteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

}
