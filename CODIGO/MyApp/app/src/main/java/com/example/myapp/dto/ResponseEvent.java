package com.example.myapp.dto;

public class ResponseEvent {

    private Boolean success;
    private String env;
    private ResponseSubEvent event;

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

}
