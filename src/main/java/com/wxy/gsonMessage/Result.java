package com.wxy.gsonMessage;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class Result<T> {
    public String type;
    public boolean isList;
    public long ftmId;
    public T data;

    public Result(String type, boolean isList,long ftmId) {
        this.type = type;
        this.isList = isList;
        this.ftmId = ftmId;
    }



}


