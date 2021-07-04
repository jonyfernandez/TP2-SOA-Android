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
import com.example.myapp.dto.RequestRegistro;
import com.example.myapp.dto.ResponseRegistro;
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

public class Registro extends AppCompatActivity {

    private EditText nombre, apellido, password, dni, email, comision, grupo;
    private Button boton_registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = findViewById(R.id.etNombre);
        apellido = findViewById(R.id.etApellido);
        dni = findViewById(R.id.etDni);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        comision = findViewById(R.id.etComision);
        grupo = findViewById(R.id.etGrupo);
        boton_registrar = findViewById(R.id.btnRegistro);

        boton_registrar.setOnClickListener(v -> {
            if(comprobarConexionInternet()) {
                if (validarCampos()) {

                    RequestRegistro request = new RequestRegistro();
                    request.setEnv("PROD");
                    request.setName(nombre.getText().toString());
                    request.setLastname(apellido.getText().toString());
                    request.setDni(Long.parseLong(dni.getText().toString()));
                    request.setEmail(email.getText().toString());
                    request.setPassword(password.getText().toString());
                    request.setCommission(Long.parseLong(comision.getText().toString()));
                    request.setGroup(Long.parseLong(grupo.getText().toString()));

                    Retrofit retrofit = new Retrofit.Builder() //hay que agregar las dependencias en build.gradle
                            .baseUrl("http://so-unlam.net.ar/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Service service = retrofit.create(Service.class);

                    Call<ResponseRegistro> call = service.register(request);
                    call.enqueue(new Callback<ResponseRegistro>() { //ejecuccion asincrónica, si fuera sincronica se queda esperando la respuesta
                        @Override
                        public void onResponse(Call<ResponseRegistro> call, Response<ResponseRegistro> response) {

                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registro Exitoso\n", Toast.LENGTH_LONG).show();

                                UsuarioLoggeado.setToken(response.body().getToken());
                                UsuarioLoggeado.setToken_refresh(response.body().getToken_refresh());

                                PedidoAPI pedido = new PedidoAPI();
                                pedido.registrarEvento("Nuevo Registro", "Registro"); //Registro el evento en la API

                                ServiceActualizacionToken.iniciarTimer();
                                Intent actualizar = new Intent(Registro.this, ServiceActualizacionToken.class);
                                startService(actualizar);

                                Intent intent = new Intent(Registro.this, MenuPrincipal.class);
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
                        public void onFailure(Call<ResponseRegistro> call, Throwable t) { //Cuando hay una mala configuracion del retrofit
                            Log.e("Fallo", t.getMessage());
                        }

                    });
                }
            }else {
                Toast.makeText(getApplicationContext(), "No se pudo completar el registro" + "su internet esta desconectada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("registro", "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceActualizacionToken.detenerTimer();
        Log.i("registro", "OnResume");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("registro", "OnStop");
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

    public boolean validarCampos(){
        String campNombre = nombre.getText().toString();
        String campApellido = apellido.getText().toString();
        String campDni = dni.getText().toString();
        String campEmail = email.getText().toString();
        String campPass = password.getText().toString();
        String campComi = comision.getText().toString();
        String campGrupo = grupo.getText().toString();

        boolean valido = true;
        if(campNombre.isEmpty()){
            nombre.setError("Debe ingresar su nombre para registrarse");
            valido = false;
        }
        if(campApellido.isEmpty()){
            apellido.setError("Debe ingresar su apellido para registrarse");
            valido = false;
        }
        if(campDni.isEmpty() || campDni.length() > 8){
            dni.setError("Numero de dni incorrecto, recuerde que no debe ingresar mas de 8 caracteres");
            valido = false;
        }
        if(campEmail.isEmpty()){
            email.setError("Debe ingresar su email para registrarse");
            valido = false;
        }
        if(campPass.isEmpty() || campPass.length() < 8){
            password.setError("Campo Password incorrecto, recuerde debe ingresar 8 caracteres o mas");
            valido = false;
        }
        if(campComi.isEmpty()){
            comision.setError("Debe ingresar la comision para registrarse");
            valido = false;
        }

        if(campGrupo.isEmpty()){
            comision.setError("Debe ingresar el numero de grupo para registrarse");
            valido = false;
        }

        return valido;
    }

}
