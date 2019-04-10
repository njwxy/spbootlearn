package com.wxy.gsonMessage;


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


