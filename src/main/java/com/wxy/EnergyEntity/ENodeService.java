package com.wxy.EnergyEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ENodeService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /*
    *     public float aVolt,bVolt,cVolt;
    public float aCurrent,bCurrent,cCurrent;
    public float freq;
    public float aAp,bAp,cAp;
    public float aPosAe,bPosAe,cPosAe,comAe;
    public int comState;
    public Date time;

    * */

    public void createPvNode(EnergyNode node) {
        //insert into pvnoderpt(devAddr,voltage,temperature,relaySate,`signal`,time) values(1,1.2,2.3,1,3,0);
        jdbcTemplate.update("INSERT INTO energynoderpt(devAddr,va,vb,vc,ia,ib,ic,freq,pa,pb,pc,ea,eb,ec,ecom,state,relaystate,time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                node.devAddr,
                node.aVolt,node.bVolt,node.cVolt,
                node.aCurrent,node.bCurrent,node.cAp,
                node.freq,
                node.aAp,node.bAp,node.cAp,
                node.aPosAe,node.bPosAe,node.cPosAe,node.comAe,
                node.comState,
                node.relayState,
                new Timestamp(node.time.getTime()));
    }

}
