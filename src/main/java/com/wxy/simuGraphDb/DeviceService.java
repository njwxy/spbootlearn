package com.wxy.simuGraphDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceReporsitory deviceReporsitory;

   // @Autowired
  //  public DeviceService(DeviceReporsitory deviceReporsitory) {
    //    this.deviceReporsitory = deviceReporsitory;
   // }

    public Device saveDevice(Device device)
    {

        Device deviceNew = deviceReporsitory.findByDevAddr(device.getDevAddr());
        if(deviceNew ==null)
            return deviceReporsitory.save(device);
        else
            return deviceNew;
    }

    public void  saveAllDevices(ArrayList<Device> devices)
    {
        deviceReporsitory.saveAll(devices);
        //deviceReporsitory.deleteAll(devices);
    }
    public void deleteAll(ArrayList<Device> devices)
    {
        deviceReporsitory.deleteAll(devices);
    }

    public void deleteAll()
    {
        deviceReporsitory.deleteAll();
    }

    public void gwAddNode(long gwAddr,long nodeAddr){
        deviceReporsitory.gwAddNode(gwAddr,nodeAddr);
    }

    public List getDevices(long gwAddr){return deviceReporsitory.getDeviceList(gwAddr);};

    public void addRelationHas(long gwAddr,long nodeAddr,int num){
        deviceReporsitory.addRelationHas(gwAddr,nodeAddr,num);
    }

    public  Device  findGwByNodeAddr(long nodeAddr)
    {
        return deviceReporsitory.findGwByNodeAddr(nodeAddr);
    }

}
