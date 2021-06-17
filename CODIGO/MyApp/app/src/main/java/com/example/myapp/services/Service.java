package com.example.myapp.services;

import com.example.myapp.dto.RequestLogin;
import com.example.myapp.dto.RequestRegistro;
import com.example.myapp.dto.ResponseLogin;
import com.example.myapp.dto.ResponseRegistro;
import  com.example.myapp.dto.RequestEvent;
import  com.example.myapp.dto.ResponseEvent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Header;

public interface Service {

    @POST("api/login") //endpoint donde se va a ir a pedir la peticion
    Call<ResponseLogin> ingresar (@Body RequestLogin requestLogin);

    @POST("api/register")
    Call<ResponseRegistro> register (@Body RequestRegistro requestRegistro);

    @POST("api/event")
    Call<ResponseEvent> registrarEvento(@Header ("token") String token, @Body RequestEvent requestEvent);

    /*@POST("api/refresh")
    Call<ResponseTokenRefresh> actualizarToken(@Header String tokenRefresh);*/
}
