package com.wxy.ftm;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mqtt")
@PropertySource("classpath:application.yml")
@Slf4j
@Setter
@Getter
public class MosConfig {
    private String Url;
    private String Topic;

    @Bean
    public int printMsConfig(){
        log.info("=====Url===="+Url+"===Topic===="+Topic);
        return  0;
    }
}
