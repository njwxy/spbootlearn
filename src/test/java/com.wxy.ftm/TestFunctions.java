package com.wxy.ftm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxy.EnergyEntity.EnergyNode;
import com.wxy.EnergyEntity.EnergyRelayState;
import com.wxy.EnergyEntity.EnergyVersion;
import com.wxy.EnergyEntity.EnergyVersionReq;
import com.wxy.gsonMessage.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TestFunctions {
    @Test
    public void testArraylist(){
        List<Long> listdata = new ArrayList<Long>();

        for(long i=20;i>0;i--)
        {
            listdata.add(i);
        }

        listdata.stream().forEach(d->System.out.print(" " + d));
        List<Long> listnew = listdata.stream().sorted().collect(Collectors.toList());
        log.info("after sort");
        listnew.stream().forEach(d->System.out.print(" " + d));
    }


    public static <T> void  getJsonData (T data,String name){

        Gson gson = new GsonBuilder().setDateFormat("yy-MM-dd HH:mm:ss").create();
        Result<T> energyVersionResult;
        energyVersionResult = new Result<T>(name,false,1);
        energyVersionResult.data = data;
        String jsonstr = gson.toJson(energyVersionResult);
        log.info(jsonstr);
       }


    @Test
     public void testEnergyNodeJson()
    {
        EnergyNode energyNode = new EnergyNode(0x23);
        energyNode.setValue(20.1f,220.5f,220.3f,
                15.3f,11.2f,10.5f,
                50.3f,2230.2f,2935f,1343.3f,
                10.5f,25.3f,19.4f,50.9f,
                1,1,new Date());
        EnergyRelayState energyRelayState = new EnergyRelayState(0x23, (short) 1);
        EnergyVersion energyVersion = new EnergyVersion(23, (short) 1,"V1.00");
        EnergyVersionReq energyVersionReq = new EnergyVersionReq(23, (short) 1);

        getJsonData(energyNode,EnergyNode.class.getSimpleName());
        getJsonData(energyRelayState,EnergyRelayState.class.getSimpleName());
        getJsonData(energyVersion,EnergyVersion.class.getSimpleName());
        getJsonData(energyVersionReq,EnergyVersionReq.class.getSimpleName());

        Result<EnergyVersion> result = new Result<EnergyVersion>(EnergyVersion.class.getSimpleName(),false,1,energyVersion);
        String retstr = result.GetGsonString();
        log.info(retstr);
    }
}
