package com.example.myapp.dto;

public class RequestRegistro {

    private String env;
    private String name;
    private String lastname;
    private long dni;
    private String email;
    private String password;
    private long comission;
    private long group;

    public String getEnv(){
        return env;
    }

    public void setEnv(String env){
        this.env = env;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getLastname(){
        return lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public long getDni(){
        return dni;
    }

    public void setDni(long dni){
        this.dni = dni;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public long getComission(){
        return comission;
    }

    public void setComission(long comission){
        this.comission = comission;
    }

    public long getGroup(){
        return group;
    }

    public void setGroup(long group){
        this.group = group;
    }
}
