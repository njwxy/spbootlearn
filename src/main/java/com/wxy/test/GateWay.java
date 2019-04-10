package com.wxy.test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/*网关类
* 1.
*
* */
public class GateWay {
    public long devAddr;
    public int heartInterval;
    public int pollingInterval;
    public boolean nodeLoad;
    public InetSocketAddress clientIpAddr;
    public Hashtable<Long,PvNode> nodeList;

    public GateWay() {
        nodeList = new Hashtable<Long,PvNode>(256);
        nodeLoad = false;
    }
    
    void removeAllNode(){
        if(nodeList.size()!=0)
        {
            Iterator<Map.Entry<Long,PvNode>> entries = nodeList.entrySet().iterator();
            while(entries.hasNext()) {
                Map.Entry<Long,PvNode> entry = entries.next();
                Long key = entry.getKey();
                System.out.println("key "+ key);
                System.out.println("remove:key = "+entry.getKey()+"--value="+entry.getValue().toString());
                //nodeList.remove(entry.getKey());
                entries.remove();
            }
            System.out.println("left size "+nodeList.size());

            /*for(Long key:nodeList.keySet())
            {
                System.out.println(nodeList.get(key).toString());
                nodeList.remove(key);  error
            }*/
        }
    }

    public InetSocketAddress getClientIpAddr() {
        return clientIpAddr;
    }

    public void setClientIpAddr(InetSocketAddress clientIpAddr) {
        this.clientIpAddr = clientIpAddr;
    }

    public static void main(String args[])
    {
        GateWay gateWay = new GateWay();
            for(int i=0;i<10;i++)
            {
                PvNode node = new PvNode(i+1);
                gateWay.nodeList.put((long) i,node);
            }
         gateWay.removeAllNode();

    }

}
