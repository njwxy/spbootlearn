package com.wxy.ftm;


import com.wxy.comm.NIOServer;
import com.wxy.pventity.PvMsgHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableConfigurationProperties
@SpringBootApplication
@ComponentScan(basePackages = {"com.wxy","com.wxy.simuGraphDb"})
@EnableNeo4jRepositories("com.wxy.simuGraphDb")
public class PvFront {
    private final static Logger log = LoggerFactory.getLogger(PvFront.class);

    public static void main(String[] args){
        SpringApplication.run(PvFront.class,args);
    }
    @Autowired
    private PvMsgHandle pvMsgHandle;

    @Autowired
    private SystemParams systemParams;

    @Bean
    CommandLineRunner startNetty(){
        return args->{
            NIOServer nioServer= new NIOServer(pvMsgHandle,systemParams.getFrontServerPort());
            nioServer.StartServer();
        };
    }

  //  public void run(String... args) throws Exception {
      //  NIOServer nioServer = new NIOServer(pvMsgHandle,12345);
  //  }
}
