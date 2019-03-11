package com.wxy.testneo4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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


    public static void main(String[] args){
        SpringApplication.run(SpringBootNeo4jApplication.class,args);
    }
}
