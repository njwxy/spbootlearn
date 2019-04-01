package com.wxy.testneo4j;


import com.wxy.comm.NIOServer;
import com.wxy.test.GateWay;
import com.wxy.test.PvMsgHandle;
import com.wxy.test.PvNode;
import com.wxy.usertest.User;
import com.wxy.usertest.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@ComponentScan(basePackages = {"com.wxy"})
public class MainController {
    private final static Logger log = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String sayHello(Model model){
        User single = new User("aaaaa", 11);
        List<User> people = new ArrayList<User>();
        User p1 = new User("xx",11);
        User p2 = new User("yy",22);
        User p3 = new User("zz",33);
        people.add(p1);
        people.add(p2);
        people.add(p3);
        model.addAttribute("singlePerson", single);
        model.addAttribute("people", people);
        return "hello";
    }

    @Bean
    CommandLineRunner runhello(){
        return args->{
            System.out.println("----------------------------hello function started--------------------");
        };
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PvMsgHandle pvMsgHandle;

    @Autowired
    // private GwConfigService gwConfigService;
            GwConfigReporsitory gwConfigReporsitory;



    @RequestMapping(value = "/nodelist/{gwaddr}")
    public String showDeviceList(Model model, @PathVariable("gwaddr") long gwaddr){
        List lstDevice =  deviceService.getDevices(gwaddr);
        model.addAttribute("devices",lstDevice);
        model.addAttribute("gwaddr",gwaddr);
        return "nodelist";
    }

    @RequestMapping(value = "setRelay/{nodeAddr}/{relayValue}")
    public String setRealy(Model model, @PathVariable("nodeAddr") long nodeaddr,@PathVariable("relayValue") short relayValue){
        String retval = pvMsgHandle.sendSetRelayState(nodeaddr,relayValue);
        model.addAttribute("result",retval);
        return "relayState";
    }

    @RequestMapping("/enroll")
    public  String login(ModelMap map){
        DeviceEnroll deviceEnroll= new DeviceEnroll(0,1,99,1,1);
        map.put("enrollmap",deviceEnroll);
        map.put("title","开通录入设备");
        return "enrollDevice";
    }

    @RequestMapping("/showNodeData/{gwAddr}")
    public String showNodeData(Model model,@PathVariable("gwAddr") long gwAddr)
    {
        GateWay gateWay = pvMsgHandle.getGateWay(gwAddr);
        if(gateWay!=null)
        {
            ArrayList<PvNode> pvNodesList = new ArrayList<PvNode>();
            int pos=0;
            for(Map.Entry<Long,PvNode> entry:gateWay.nodeList.entrySet() ){
                pvNodesList.add(entry.getValue());
            }
            model.addAttribute("pvNodes",pvNodesList);
            return "showNodeData";
        }
        return "errorNotFindGateWay";
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public String add(Model model, @Valid DeviceEnroll deviceEnroll, BindingResult result){
        long gwAddr = deviceEnroll.getGwAddr();
        long nodeAddr = deviceEnroll.getNodeAddr();
        Integer nodeNum = deviceEnroll.getNodeNum();
        model.addAttribute("gwAddr",gwAddr);
        model.addAttribute("nodeAddr",nodeAddr);
        model.addAttribute("nodeNum",nodeNum);
        if(result.hasErrors()){
            model.addAttribute("MSG", "出错啦！");
            String defaultMessage = result.getFieldError().getDefaultMessage();
            model.addAttribute("defaultMessage",defaultMessage);
        }else{
            GwConfig gwConfig = new GwConfig();
            gwConfig.setGwAddr(gwAddr);
            gwConfig.setHeartInterval((short) deviceEnroll.getHeartInterval());
            gwConfig.setPollingInterval((short)deviceEnroll.getPollingInterval());
            gwConfigReporsitory.save(gwConfig);
            addGroupDevices(gwAddr,nodeAddr,nodeNum);
            model.addAttribute("MSG", "提交成功！");
        }
        return "result";
    }


    void addGroupDevices(long gwAddr,long nodeAddr,int num)
    {
        ArrayList<Device> deviceArrayList = new ArrayList<Device>();
        Device device = new Device();
        device.setDevAddr(gwAddr);
        device.setType(0);
        device.setProduceTime(new Date());
        deviceArrayList.add(device);

        for(long i=0;i<num;i++)
        {
            long devnum= i+nodeAddr;
            device = new Device();
            device.setProduceTime(new Date());
            device.setType(1);
            device.setDevAddr(devnum);
            deviceArrayList.add(device);
        }
        deviceService.saveAllDevices(deviceArrayList);
        deviceService.addRelationHas(gwAddr,nodeAddr,num);
    }



    @GetMapping("/list")
    public String queryUsers(Model model){
        List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from user");
        model.addAttribute("userlist",list);
        return  "userList";
    }

    @Bean
    CommandLineRunner startNetty(){
        return args->{
            NIOServer nioServer= new NIOServer(pvMsgHandle,12345);
        };
    }

}
