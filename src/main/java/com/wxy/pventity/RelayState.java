package com.wxy.pventity;

public class RelayState {
    public long gwAddr;
    public long devAddr;
    public int state;


    public RelayState(long gwAddr, long devAddr, int state) {
        this.gwAddr = gwAddr;
        this.devAddr = devAddr;
        this.state = state;
    }

    public long getGwAddr() {
        return gwAddr;
    }

    public void setGwAddr(long gwAddr) {
        this.gwAddr = gwAddr;
    }

    public long getDevAddr() {
        return devAddr;
    }

    public void setDevAddr(long devAddr) {
        this.devAddr = devAddr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
