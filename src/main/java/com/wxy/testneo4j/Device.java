package com.wxy.testneo4j;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.DateLong;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.GroupSequence;
import java.util.Date;

@NodeEntity
public class Device {
    @GraphId
    private Long id;
    private Long devAddr;
    /*
    *  0:gateway 1:1101 2:plc
    * */
    private int type;
    @DateLong
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date produceTime;




    public Device() {
    }

    public Device(Long id, Long devAddr, int type, Date produceTime) {
        this.id = id;
        this.devAddr = devAddr;
        this.type = type;
        this.produceTime = produceTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDevAddr() {
        return devAddr;
    }

    public void setDevAddr(Long devAddr) {
        this.devAddr = devAddr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(Date produceTime) {
        this.produceTime = produceTime;
    }



    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", devAddr=" + devAddr +
                ", type=" + type +
                ", produceTime=" + produceTime +
                '}';
    }
}

