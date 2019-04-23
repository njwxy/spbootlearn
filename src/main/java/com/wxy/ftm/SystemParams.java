package com.wxy.ftm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "systemparam")
@PropertySource("classpath:application.yml")
public class SystemParams {
    private String webServerIp;
    private int webServerPort;
    private int heartInterval;
    private int pollingInterval;
    private int frontServerPort; /* 前置机通信端口 */
    private int webListenPort; /*  服务器消息监听端口 */
    private long fmtId;        /* 前置机id 号 */

    public long getFmtId() {
        return fmtId;
    }

    public void setFmtId(long fmtId) {
        this.fmtId = fmtId;
    }

    public int getWebListenPort() {
        return webListenPort;
    }

    public void setWebListenPort(int webListenPort) {
        this.webListenPort = webListenPort;
    }

    public int getFrontServerPort() {
        return frontServerPort;
    }

    public void setFrontServerPort(int frontServerPort) {
        this.frontServerPort = frontServerPort;
    }

    public int getHeartInterval() {
        return heartInterval;
    }

    public void setHeartInterval(int heartInterval) {
        this.heartInterval = heartInterval;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public String getWebServerIp() {
        return webServerIp;
    }

    public void setWebServerIp(String webServerIp) {
        this.webServerIp = webServerIp;
    }

    public int getWebServerPort() {
        return webServerPort;
    }

    public void setWebServerPort(int webServerPort) {
        this.webServerPort = webServerPort;
    }
}
