package com.wxy.ftm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TestFunctions {
    @Test
    public void testArraylist(){
        List<Long> listdata = new ArrayList<Long>();

        for(long i=20;i>0;i--)
        {
            listdata.add(i);
        }

        listdata.stream().forEach(d->System.out.print(" " + d));
        List<Long> listnew = listdata.stream().sorted().collect(Collectors.toList());
        log.info("after sort");
        listnew.stream().forEach(d->System.out.print(" " + d));


    }
}
