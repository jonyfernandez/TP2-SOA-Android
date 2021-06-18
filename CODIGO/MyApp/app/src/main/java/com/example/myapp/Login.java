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

import com.example.myapp.dto.RequestLogin;
import com.example.myapp.dto.ResponseLogin;
import com.example.myapp.services.Service;

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
                                    /*Acá se puede mostrar un msj que tal persona inició session*/
                                    /*Y tengo que pasar a la otra actividad, osea al menu principal*/
                                    /*Tambien podesmos registrar el evento en la api*/
                                    Toast.makeText(getApplicationContext(), "Usuario", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.e("Fallo", response.toString());
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseLogin> call, Throwable t) { //Cuando hay una mala configuracion del retrofit
                                Log.e("Fallo", t.getMessage());
                            }

                        });
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "su internet esta desconectada", Toast.LENGTH_SHORT).show();
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
