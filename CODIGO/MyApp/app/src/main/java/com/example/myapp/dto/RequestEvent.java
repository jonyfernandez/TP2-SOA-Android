package com.example.myapp.dto;

public class RequestEvent {

    private String env;
    private String type_events;
    private String description;

    public String getEnv(){
        return env;
    }

    public void setEnv(String env){
        this.env = env;
    }

    public String getType_events(){
        return type_events;
    }

    public void setType_events(String type_events){
        this.type_events = type_events;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
