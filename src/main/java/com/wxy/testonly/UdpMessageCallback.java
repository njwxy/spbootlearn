package com.wxy.testonly;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface UdpMessageCallback {
    public void messageHandle(DatagramSocket socket, DatagramPacket packet) throws IOException;
}
