package com.example.myapp.dto;

public class ResponseSubEvent {

    private String type_events;
    private long dni;
    private String descripcion;
    private long id;

    public String getType_events(){
        return type_events;
    }

    public void setType_events(String type_events){
        this.type_events = type_events;
    }

    public long getDni(){
        return dni;
    }

    public void setDni(long dni){
        this.dni = dni;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }
}
