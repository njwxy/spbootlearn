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
/*
    energy-web-server-ip: "10.1.3.35"
    energy-web-server-port: 12333
    energy-web-listen-port: 12344
    energy-front-server-port: 12389
  */

    private String energyWebServerIp;
    private int energyWebServerPort;            /* 主站服务器端口*/
    private int energyFrontServerPort;          /* 前置机端口*/
    private int energyWebListenPort;            /* 前置机监听主站服务器下发命令端口 */

    private long fmtId;        /* 前置机id 号 */
}
