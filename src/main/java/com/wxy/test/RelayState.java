package com.wxy.test;

public class RelayState {
    public long devAddr;
    public int state;

    public RelayState(long devAddr, int state) {
        this.devAddr = devAddr;
        this.state = state;
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
