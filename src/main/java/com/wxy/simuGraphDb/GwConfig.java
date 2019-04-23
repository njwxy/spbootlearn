package com.wxy.simuGraphDb;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class GwConfig {
    @GraphId
    private Long Id;

    private   long gwAddr;
    private  short pollingInterval;
    private  short heartInterval;

    public GwConfig() {
    }

    public long getGwAddr() {
        return gwAddr;
    }

    public void setGwAddr(long gwAddr) {
        this.gwAddr = gwAddr;
    }

    public short getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(short pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public short getHeartInterval() {
        return heartInterval;
    }

    public void setHeartInterval(short heartInterval) {
        this.heartInterval = heartInterval;
    }
}
