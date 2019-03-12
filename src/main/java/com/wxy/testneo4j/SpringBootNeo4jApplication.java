package com.wxy.testneo4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@SpringBootApplication
public class SpringBootNeo4jApplication {
    private final static Logger log = LoggerFactory.getLogger(SpringBootNeo4jApplication.class);
    UserService userService;

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

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
        return "/hello";
    }

    @RequestMapping(value = "/new")
    public String saveOrUpdateUser(){
    //    User user = new User((long) 1,"aaa",20);
     //   userService.saveOrUpdate(user);
      //  System.out.println(user.toString());
       // return user.toString();
        return "/hello";
    }

    @Bean
    CommandLineRunner demo(){
        return args -> {
            User user = new User();
            user.setAge(25);
            user.setName("aaabbb");
            userService.saveOrUpdate(user);
            log.info(user.toString());
            log.info("demo started -----------------------------------------------!!!");

            List<User> userList= userService.getUserList();
            userList.forEach(user1->System.out.println(user1));

        };
    }





    public static void main(String[] args){
        SpringApplication.run(SpringBootNeo4jApplication.class,args);
    }
}
