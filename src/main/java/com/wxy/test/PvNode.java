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


    public long getDevAddr() {
        return devAddr;
    }

    public void setDevAddr(long devAddr) {
        this.devAddr = devAddr;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public byte getRelaySate() {
        return relaySate;
    }

    public void setRelaySate(byte relaySate) {
        this.relaySate = relaySate;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PvNode{" +
                "devAddr=" + devAddr +
                ", voltage=" + voltage +
                ", temperature=" + temperature +
                ", relaySate=" + relaySate +
                ", signal=" + signal +
                ", time=" + time +
                '}';
    }
}
