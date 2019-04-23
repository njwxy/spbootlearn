package com.wxy.testonly;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.wxy.gsonMessage.Result;
import com.wxy.pventity.PvNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestJavaGson {
    private final static Logger log = LoggerFactory.getLogger(TestJavaGson.class);

/*
    public static <T> Result<T> fromJsonObject(Reader reader, Class<T> clazz)
    {
        Type type = new ParameterizedTypeImpl(Result.class, new Class[]{clazz});
        Gson gson=new GsonBuilder().create();
        return gson.fromJson(reader, type);
    }



    public static <T> Result<List<T>> fromJsonArray(Reader reader, Class<T> clazz)
    {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(Result.class, new Type[]{listType});
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(reader, type);
    }

    */

    public static Type parseJson(String outstr)
    {
        Gson gson = new GsonBuilder().create();

        JsonElement je = new JsonParser().parse(outstr);
        String retype  = je.getAsJsonObject().get("type").getAsString();
        boolean isList = je.getAsJsonObject().get("isList").getAsBoolean();

        switch (retype)
        {
            case "PvNode":
                System.out.println("is Pvnode");
                if(isList)
                {
                    Type userListType = new TypeToken<Result<List<PvNode>>>(){}.getType();
                    Result<List<PvNode>> userListResult = gson.fromJson(outstr,userListType);
                    List<PvNode> pvNodes = userListResult.data;
                    pvNodes.stream().forEach(node->System.out.println(node.toString()));
                    return (Type) pvNodes;
                }
                else
                {
                    Type userType = new TypeToken<Result<PvNode>>(){}.getType();
                    Result<PvNode> userResult = gson.fromJson(outstr,userType);
                    PvNode pvNode1 = userResult.data;
                    System.out.println(pvNode1.toString());
                    return (Type) pvNode1;
                }
            case "getNodeAddrAck":
                if(isList==true)
                {
                    Type userListType = new TypeToken<Result<List<Long>>>(){}.getType();
                    Result<List<Long>> userListResult = gson.fromJson(outstr,userListType);
                    List<Long> nodeList = userListResult.data;
                    System.out.println();
                    System.out.println();
                    nodeList.stream().forEach(node->System.out.print(node.toString()+" "));
                    System.out.println();
                }
                else
                {
                    log.error("getNodeAddrAck must be list format.");
                }

                break;


            default:
                System.out.println("not found");
                break;
        }
        return null;
    }



    public static void main(String args[]){

       /*
        {
            Result<Long> result = new Result<Long>("getNodeAddr", false);
            result.data = new Long(100);
            Gson gson = new GsonBuilder().create();
            String outstr = gson.toJson(result);
            System.out.println(outstr);
        }*/

        Result<List<Long>> result = new Result<List<Long>>("getNodeAddrAck", true,1);
        result.data = new ArrayList<Long>();
        for(long i=101;i<200;i++)
        {
            result.data.add(new Long(i));
        }

        Gson gson = new GsonBuilder().create();
        String outstr = gson.toJson(result);
        System.out.println(outstr);
        parseJson(outstr);

    }



    public static void main1(String args[]){
        Result <List<PvNode>> result = new Result<List<PvNode>>(PvNode.class.getSimpleName(),true,1);
        result.data = new ArrayList<>();
        for(int i=0;i<10;i++) {
            PvNode pvNode = new PvNode(123+i);
            pvNode.setPara(42,25, (byte) 1,123,new Date());
            result.data.add(pvNode);
        }

        Gson gson = new GsonBuilder().create();
        String  outstr = gson.toJson(result);
        System.out.println(outstr);






        /*
        Type userType = new TypeToken<Result<User>>(){}.getType();
        Result<PvNode> userResult = gson.fromJson(outstr,userType);
        User user = userResult.data;*/



    }
}
