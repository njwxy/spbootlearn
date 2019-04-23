package com.wxy.testneo4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UdpServer implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(UdpServer.class);
    UdpMessageCallback udpMessageCallback;

    private int portNum;
    private DatagramSocket socket=null;
    private DatagramPacket packet;


    public UdpServer(int portNum,UdpMessageCallback udpMessageCallback) {
        this.portNum = portNum;
        this.udpMessageCallback = udpMessageCallback;
        log.info("udp server start at "+portNum);
        try {
            this.socket = new DatagramSocket(portNum);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(DatagramPacket datagramPacket) throws IOException {
        if(socket!=null)
            socket.send(datagramPacket);
    }


    @Override
    public void run() {

        try {

            byte[] recvData = new byte[8192];
            packet = new DatagramPacket(recvData, recvData.length);
            while (true) {
                socket.receive(packet);
                if(udpMessageCallback!=null)
                    udpMessageCallback.messageHandle(socket,packet);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

