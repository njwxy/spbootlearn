package com.wxy.EnergyEntity;

import java.net.InetSocketAddress;
import java.util.Date;

public class EnergyNode {
    public long devAddr;   /* 32 bit addr*/
    public InetSocketAddress clientIpAddr;  // node IP address + port
    public int keepLiveTime;  // scan all of node ,3 minute not receive message change remove the node

    public float aVolt,bVolt,cVolt;
    public float aCurrent,bCurrent,cCurrent;
    public float freq;
    public float aAp,bAp,cAp;
    public float aPosAe,bPosAe,cPosAe,comAe;
    public int comState;
    public int relayState;
    public Date time;


    public EnergyNode(long devAddr) {
        this.devAddr = devAddr;
        keepLiveTime = 600;  // 600 second default;
    }

    public void setValue( float aVolt, float bVolt, float cVolt,
                          float aCurrent, float bCurrent, float cCurrent,
                          float freq,
                          float aAp, float bAp, float cAp,
                          float aPosAe, float bPosAe, float cPosAe, float comAe,
                          int comState,
                          int relayState,
                          Date time)
    {
       // keepLiveTime = 600;// default 600 second keep live time;
      // this.devAddr = devAddr;
        this.aVolt = aVolt;
        this.bVolt = bVolt;
        this.cVolt = cVolt;
        this.aCurrent = aCurrent;
        this.bCurrent = bCurrent;
        this.cCurrent = cCurrent;
        this.freq = freq;
        this.aAp = aAp;
        this.bAp = bAp;
        this.cAp = cAp;
        this.aPosAe = aPosAe;
        this.bPosAe = bPosAe;
        this.cPosAe = cPosAe;
        this.comAe = comAe;
        this.comState = comState;
        this.relayState = relayState;
        this.time = time;
    }


    public long getDevAddr() {
        return devAddr;
    }

    public void setDevAddr(long devAddr) {
        this.devAddr = devAddr;
    }

    public float getaVolt() {
        return aVolt;
    }

    public int getRelayState() {
        return relayState;
    }

    public void setRelayState(int relayState) {
        this.relayState = relayState;
    }

    public void setaVolt(float aVolt) {
        this.aVolt = aVolt;
    }

    public float getbVolt() {
        return bVolt;
    }

    public void setbVolt(float bVolt) {
        this.bVolt = bVolt;
    }

    public float getcVolt() {
        return cVolt;
    }

    public void setcVolt(float cVolt) {
        this.cVolt = cVolt;
    }

    public float getaCurrent() {
        return aCurrent;
    }

    public void setaCurrent(float aCurrent) {
        this.aCurrent = aCurrent;
    }

    public float getbCurrent() {
        return bCurrent;
    }

    public void setbCurrent(float bCurrent) {
        this.bCurrent = bCurrent;
    }

    public float getcCurrent() {
        return cCurrent;
    }

    public void setcCurrent(float cCurrent) {
        this.cCurrent = cCurrent;
    }

    public float getFreq() {
        return freq;
    }

    public void setFreq(float freq) {
        this.freq = freq;
    }

    public float getaAp() {
        return aAp;
    }

    public void setaAp(float aAp) {
        this.aAp = aAp;
    }

    public float getbAp() {
        return bAp;
    }

    public void setbAp(float bAp) {
        this.bAp = bAp;
    }

    public float getcAp() {
        return cAp;
    }

    public void setcAp(float cAp) {
        this.cAp = cAp;
    }

    public float getaPosAe() {
        return aPosAe;
    }

    public void setaPosAe(float aPosAe) {
        this.aPosAe = aPosAe;
    }

    public float getbPosAe() {
        return bPosAe;
    }

    public void setbPosAe(float bPosAe) {
        this.bPosAe = bPosAe;
    }

    public float getcPosAe() {
        return cPosAe;
    }

    public void setcPosAe(float cPosAe) {
        this.cPosAe = cPosAe;
    }

    public float getComAe() {
        return comAe;
    }

    public void setComAe(float comAe) {
        this.comAe = comAe;
    }

    public int getComState() {
        return comState;
    }

    public void setComState(int comState) {
        this.comState = comState;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public InetSocketAddress getClientIpAddr() {
        return clientIpAddr;
    }
}
