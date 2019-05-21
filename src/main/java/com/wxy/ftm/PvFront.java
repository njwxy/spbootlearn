package com.wxy.ftm;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableConfigurationProperties
@SpringBootApplication
@ComponentScan(basePackages = {"com.wxy.comm","com.wxy.ftm","com.wxy.simuGraphDb","com.wxy.mqtt","com.wxy.pventity","com.wxy.EnergyEntity"})
@EnableNeo4jRepositories("com.wxy.simuGraphDb")
@Slf4j
public class PvFront {
    private static ApplicationContext applicationContext;

     public static void main(String[] args){
      applicationContext =  SpringApplication.run(PvFront.class,args);
   //  ApplicationContextProvider applicationContextProvider
     //        = applicationContext.getBean(ApplicationContextProvider.class);
    //  applicationContextProvider.setApplicationContext(applicationContext);
       // MqttProducer.setApplicationContext(applicationContext);
    }
   /* @Autowired
    private PvMsgHandle pvMsgHandle;

    @Autowired
    private SystemParams systemParams;
*/
  /*  @Bean
    CommandLineRunner startNetty(){
        return args->{
            NIOServer nioServer= new NIOServer(pvMsgHandle,systemParams.getFrontServerPort());
            nioServer.StartServer();
        };
    }
*/
  //  public void run(String... args) throws Exception {
      //  NIOServer nioServer = new NIOServer(pvMsgHandle,12345);
  //  }
}
