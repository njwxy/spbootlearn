
package com.wxy.testonly;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxy.simuGraphDb.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ.*;

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

        Gson gson = new GsonBuilder().setDateFormat("yy-MM-dd HH:mm:ss").create();
        String listStr = gson.toJson(devices);
        System.out.println(listStr);


        return;
    }
}
