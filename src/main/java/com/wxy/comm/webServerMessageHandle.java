package com.wxy.comm;

import com.wxy.test.PvMsgHandle;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class webServerMessageHandle implements UdpMessageCallback {
    PvMsgHandle pvMsgHandle;

    public webServerMessageHandle(PvMsgHandle pvMsgHandle) {
        this.pvMsgHandle = pvMsgHandle;
    }

    @Override
    public void messageHandle(DatagramSocket socket, DatagramPacket packet) throws IOException {

    }
}
