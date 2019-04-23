package com.wxy.simuGraphDb;

public class DeviceEnroll {
    private long gwAddr;
    private long nodeAddr;
    private int nodeNum;
    private int pollingInterval;
    private int heartInterval;

    public DeviceEnroll(long gwAddr, long nodeAddr, int nodeNum,int pollingInterval,int heartInterval) {
        this.gwAddr = gwAddr;
        this.nodeAddr = nodeAddr;
        this.nodeNum = nodeNum;
        this.pollingInterval = pollingInterval;
        this.heartInterval = heartInterval;
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

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public int getHeartInterval() {
        return heartInterval;
    }

    public void setHeartInterval(int heartInterval) {
        this.heartInterval = heartInterval;
    }
}
