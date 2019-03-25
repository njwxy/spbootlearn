package com.wxy.testneo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GwConfigService {
    @Autowired
    private GwConfigReporsitory gwConfigReporsitory;

    public GwConfig saveGwConfig(GwConfig gwConfig){
       return gwConfigReporsitory.save(gwConfig);
    }
}
