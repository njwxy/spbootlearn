package com.wxy.testonly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wxy.test.PvNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestJsonNode {

    public static void main(String [] argv){
        ArrayList<PvNode> pvNodes = new ArrayList<>();

        for(int i=0;i<10;i++)
        {
            PvNode pvNode = new PvNode(i);
            pvNode.setPara(3.5f,25,(byte)1,15,new Date());
            pvNodes.add(pvNode);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String outval = mapper.writeValueAsString(pvNodes);
            System.out.println(outval);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }




    }

}
