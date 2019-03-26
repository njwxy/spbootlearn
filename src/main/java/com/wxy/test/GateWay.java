package com.wxy.test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Hashtable;

/*网关类
* 1.
*
* */
public class GateWay {
    public long devAddr;
    public int heartInterval;
    public int pollingInterval;
    public InetSocketAddress clientIpAddr;
    public Hashtable<Long,PvNode> nodeList;

    public GateWay() {
        nodeList = new Hashtable<Long,PvNode>(256);
    }

    public InetSocketAddress getClientIpAddr() {
        return clientIpAddr;
    }

    public void setClientIpAddr(InetSocketAddress clientIpAddr) {
        this.clientIpAddr = clientIpAddr;
    }
}
