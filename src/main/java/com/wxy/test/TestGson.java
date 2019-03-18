
package com.wxy.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxy.testneo4j.Device;
import com.wxy.testneo4j.SpringBootNeo4jApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.*;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.zeromq.ZMQ.REP;
import static org.zeromq.ZMQ.context;


public class TestGson {
    private final static Logger log = LoggerFactory.getLogger(TestGson.class);

    public void testZmq()
    {
        Context context = context(1);
        Socket responder = context.socket(REP);
        while(!Thread.currentThread().isInterrupted())
        {

        }
    }

    public static void main(String args[])
    {
        log.info("test gson start");


        List<Device> devices = new ArrayList<Device>();
        for(long i=0;i<20;i++)
            devices.add(new Device(i,i,1,new Date()));

        Gson gson = new GsonBuilder().setDateFormat("yy-mm-dd").create();
        //Gson gson = new Gson();
        String listStr = gson.toJson(devices);
        System.out.println(listStr);


        return;
    }
}
