package com.wxy.test;

import java.util.HashMap;

public class TestHashList {
    public HashMap<Long,EnergyNode> nodelist;

    public TestHashList() {
        this.nodelist = new HashMap<>(8192);
    }

    public EnergyNode getNode(long devAddr)
    {
        return nodelist.get(devAddr);

    }
}
