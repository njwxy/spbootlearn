package com.wxy.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PvNodeService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void createPvNode(PvNode node) {
        //insert into pvnoderpt(devAddr,voltage,temperature,relaySate,`signal`,time) values(1,1.2,2.3,1,3,0);
        jdbcTemplate.update("INSERT INTO pvnoderpt(devAddr,voltage,temperature,relaySate,`signal`,time) VALUES (?,?,?,?,?,?)",node.devAddr,node.voltage,node.temperature,node.relayState,node.signal,
                new Timestamp(node.time.getTime()));
    }
}
