package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.dto.ErrorResponse;
import com.example.myapp.dto.RequestLogin;
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

public class Login extends AppCompatActivity {

    private EditText email, password;
    private Button boton_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.idEmail);
        password = findViewById(R.id.idPassword);
        boton_login = (Button) findViewById(R.id.btnLogin);

        boton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if(comprobarConexionInternet()) {
                    if (validarCampos()) {

                        RequestLogin request = new RequestLogin();
                        request.setEmail(email.getText().toString());
                        request.setPassword(password.getText().toString());

                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())
                                .baseUrl("http://so-unlam.net.ar/api/")
                                .build();

                        Service service = retrofit.create(Service.class);

                        Call<ResponseLogin> call = service.ingresar(request);
                        call.enqueue(new Callback<ResponseLogin>() { //ejecuccion asincrónica, si fuera sincronica se queda esperando la respuesta
                            @Override
                            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                                if (response.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(), "Bienvenido" , Toast.LENGTH_LONG).show();

                                    PedidoAPI pedido = new PedidoAPI();
                                    pedido.registrarEvento("Ingreso Login", "Login"); //Registro el evento en la API
                                    UsuarioLoggeado.setToken(response.body().getToken());
                                    UsuarioLoggeado.setToken_refresh(response.body().getToken_refresh());

                                    ServiceActualizacionToken.iniciarTimer();
                                    Intent actualizar = new Intent(Login.this, ServiceActualizacionToken.class);
                                    startService(actualizar);

                                    Intent intent = new Intent(Login.this, MenuPrincipal.class);
                                    startActivity(intent);

                                } else if(response.body() == null){
                                    Gson gson = new Gson();
                                    Type type =  new TypeToken<ErrorResponse>(){}.getType();
                                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                                    Toast.makeText(getApplicationContext(), errorResponse.getMsg(), Toast.LENGTH_LONG).show();
                                    Log.i("mensajeError",errorResponse.getMsg());
                                }else {
                                    Log.e("failure",response.message());
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseLogin> call, Throwable t) { //Cuando hay una mala configuracion del retrofit
                                Log.e("Fallo", t.getMessage());
                            }

                        });
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Su internet esta desconectada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Metodo para comprobar si el smartphone tiene conexion a internet
     * retorna true o false segun el estado de la conexion
     */

    private boolean comprobarConexionInternet() {

        boolean conectado = false;

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            conectado = true;
        }else
            conectado = false;

        return conectado;

    }

    /**
     * Metodo onClick del boton registrarse.
     * Si presiono el boton registrarse me lleva a la actividad registro.
     */

    public void IrARegistro(View view) {
        //Si hay internet me voy a la actividad Registro
        if(comprobarConexionInternet()) {
            Intent intent = new Intent(Login.this, Registro.class);
            startActivity(intent);
        }else
            Toast.makeText(getApplicationContext(), "No se pudo ingresar al Registro, " + "su internet esta desconectada", Toast.LENGTH_SHORT).show();
    }

    /**
     * Metodo para validar los campos que ingresa el usuario.
     */

    public boolean validarCampos(){

        String campEmail = email.getText().toString();
        String campPass = password.getText().toString();
        boolean valido = true;

        if(campEmail.isEmpty() ){
            email.setError("Debe ingresar su E-mail");
            valido = false;
        }
        if(campPass.isEmpty() || campPass.length() < 8){
            password.setError("Campo password INCORRECTO, recuerde debe ingresar 8 caracteres o mas");
            valido = false;
        }
        return valido;
    }


}
