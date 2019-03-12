package com.wxy.testneo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {


    private DeviceReporsitory deviceReporsitory;

    @Autowired
    public DeviceService(DeviceReporsitory deviceReporsitory) {
        this.deviceReporsitory = deviceReporsitory;
    }

    public Device saveDevice(Device device)
    {
        Device deviceNew = deviceReporsitory.findByDevAddr(device.getDevAddr());
        if(deviceNew ==null)
            return deviceReporsitory.save(device);
        else
            return deviceNew;
    }

    public void deleteAll()
    {
        deviceReporsitory.deleteAll();
    }

    public void gwAddNode(long gwAddr,long nodeAddr){
        deviceReporsitory.gwAddNode(gwAddr,nodeAddr);
    }
}
