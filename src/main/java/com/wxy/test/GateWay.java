package com.wxy.test;

import java.util.ArrayList;
import java.util.Hashtable;

/*网关类
* 1.
*
* */
public class GateWay {
    public long devAddr;
    public int heartInterval;
    public int pollingInterval;
    public Hashtable<Long,PvNode> nodeList;

    public GateWay() {
        nodeList = new Hashtable<Long,PvNode>(256);
    }
}
