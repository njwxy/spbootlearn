package com.wxy.testneo4j;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxy.comm.MessageHandler;
import com.wxy.comm.NIOServer;
import com.wxy.test.GateWay;
import com.wxy.test.PvMsgHandle;
import com.wxy.test.PvNode;
import com.wxy.test.SystemParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

@EnableConfigurationProperties
@SpringBootApplication
@ComponentScan(basePackages = {"com.wxy"})
public class SpringBootNeo4jApplication {
    private final static Logger log = LoggerFactory.getLogger(SpringBootNeo4jApplication.class);

    public static void main(String[] args){
        SpringApplication.run(SpringBootNeo4jApplication.class,args);
    }
    @Autowired
    private PvMsgHandle pvMsgHandle;

    @Autowired
    private SystemParams systemParams;

    @Bean
    CommandLineRunner startNetty(){
        return args->{
            NIOServer nioServer= new NIOServer(pvMsgHandle,systemParams.getFrontServerPort());
        };
    }

  //  public void run(String... args) throws Exception {
      //  NIOServer nioServer = new NIOServer(pvMsgHandle,12345);
  //  }
}
