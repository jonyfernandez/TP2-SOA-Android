package com.example.myapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.myapp.dto.ErrorResponse;
import com.example.myapp.dto.RequestEvent;
import com.example.myapp.dto.ResponseEvent;
import com.example.myapp.dto.ResponseLogin;
import com.example.myapp.dto.UsuarioLoggeado;
import com.example.myapp.services.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PedidoAPI {

    private Context context;
    public PedidoAPI(Context context){
        this.context=context;
    }
    public PedidoAPI(){

    }

    public void registrarEvento(final String descripcion, final String type_events){

        RequestEvent requestEvent = new RequestEvent();
        requestEvent.setEnv("PROD");
        requestEvent.setDescription(descripcion);
        requestEvent.setType_events(type_events);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://so-unlam.net.ar/api/")
                .build();

        Service service = retrofit.create(Service.class);

        String token = UsuarioLoggeado.getToken();
        Call<ResponseEvent> call = service.registrarEvento("Bearer "+ token, requestEvent);
        call.enqueue(new Callback<ResponseEvent>() {
            @Override
            public void onResponse(Call<ResponseEvent> call, Response<ResponseEvent> response) {
                if (response.isSuccessful()){
                    Log.i("mensajeSuccess",type_events + "registrado");
                }else if(response.body() == null){
                    Gson gson = new Gson();
                    Type type =  new TypeToken<ErrorResponse>(){}.getType();
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                    Toast.makeText(context, errorResponse.getMsg(), Toast.LENGTH_LONG).show();
                    Log.i("mensajeError",errorResponse.getMsg());
                }else{
                    Log.i("mensajeFallo","fallo "+ descripcion);
                }

            }

            @Override
            public void onFailure(Call<ResponseEvent> call, Throwable t) { //Cuando hay una mala configuracion del retrofit
                Log.e("Fallo", t.getMessage());
            }
        });

    }

    public void actualizarToken(){

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://so-unlam.net.ar/api/")
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseLogin> call = service.actualizarToken("Bearer "+UsuarioLoggeado.getToken_refresh());
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful()){
                    UsuarioLoggeado.setToken(response.body().getToken());
                    UsuarioLoggeado.setToken_refresh(response.body().getToken_refresh());
                    Log.i("mensajeSuccess", "Token actualizado");
                }else if(response.body() == null){
                    Gson gson = new Gson();
                    Type type =  new TypeToken<ErrorResponse>(){}.getType();
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                    Toast.makeText(context, errorResponse.getMsg(), Toast.LENGTH_LONG).show();
                    Log.i("mensajeError",errorResponse.getMsg());
                }else{
                    Log.i("mensajeFallo","fallo ");
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.e("Fallo", t.getMessage());
            }
        });

    }
}
