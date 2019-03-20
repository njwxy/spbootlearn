package com.wxy.testneo4j;

public class DeviceEnroll {
    private long gwAddr;
    private long nodeAddr;
    private int nodeNum;

    public DeviceEnroll(long gwAddr, long nodeAddr, int nodeNum) {
        this.gwAddr = gwAddr;
        this.nodeAddr = nodeAddr;
        this.nodeNum = nodeNum;
    }

    public DeviceEnroll() {
    }

    public long getGwAddr() {
        return gwAddr;
    }

    public void setGwAddr(long gwAddr) {
        this.gwAddr = gwAddr;
    }

    public long getNodeAddr() {
        return nodeAddr;
    }

    public void setNodeAddr(long nodeAddr) {
        this.nodeAddr = nodeAddr;
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
    }
}
