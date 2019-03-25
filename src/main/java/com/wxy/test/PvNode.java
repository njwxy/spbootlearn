package com.wxy.test;

import java.util.Date;

public class PvNode {
    public long devAddr;
    public float voltage;
    public float temperature;
    public byte relaySate;
    public int signal;
    public Date time;

    public PvNode(long devAddr) {
        this.devAddr = devAddr;
    }

    public void setPara(float voltage, float temperature, byte relaySate, int signal, Date time) {
        this.voltage = voltage;
        this.temperature = temperature;
        this.relaySate = relaySate;
        this.signal = signal;
        this.time = time;
    }

}
