package com.example.myapp.dto;

public class ResponseEvent {

    private Boolean success;
    private String env;
    private ResponseSubEvent event;
    private String msg;

    public Boolean getSuccess(){
        return success;
    }

    public void setSuccess(Boolean success){
        this.success = success;
    }

    public String getEnv(){
        return env;
    }

    public void setEnv(String env){
        this.env = env;
    }

    public ResponseSubEvent getEvent(){
        return event;
    }

    public void setSuccess(ResponseSubEvent event){
        this.event = event;
    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String Msg){
        this.msg = msg;
    }
}
