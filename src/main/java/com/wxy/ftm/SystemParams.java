package com.wxy.ftm;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "systemparam")
@PropertySource("classpath:application.yml")
@Slf4j
@Getter
@Setter

public class SystemParams {
    private String webServerIp;
    private int webServerPort;
    private int heartInterval;
    private int pollingInterval;
    private int frontServerPort; /* 前置机通信端口 */
    private int webListenPort; /*  服务器消息监听端口 */
    private long fmtId;        /* 前置机id 号 */
}
