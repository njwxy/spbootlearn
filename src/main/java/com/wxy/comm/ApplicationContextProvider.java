package com.wxy.comm;

import com.wxy.ftm.SystemParams;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(String name,Class<T> aClass){
        System.out.println("context is "+context);
        return context.getBean(name,aClass);
    }

    public static <T> T getBean(Class<T> aClass){
        return context.getBean(aClass);
    }
}