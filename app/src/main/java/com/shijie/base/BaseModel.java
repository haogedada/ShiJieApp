package com.shijie.base;

import java.io.Serializable;

public class BaseModel<T> implements Serializable {

    // http 状态码
    private int code;
    // 返回信息
    private String msg;
    // 返回的数据
    private T data;

    public BaseModel() {
    }

    public BaseModel(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public boolean isSuccess(){
        return code == 200||code==199;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
