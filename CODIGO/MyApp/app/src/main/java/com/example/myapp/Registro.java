package com.example.myapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.dto.RequestRegistro;
import com.example.myapp.dto.ResponseRegistro;
import com.example.myapp.services.Service;

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
        //txtResp = findViewById(R.id.textrespuesta);
        boton_registrar = findViewById(R.id.btnRegistro);

        boton_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){

                    final RequestRegistro request = new RequestRegistro();
                    request.setEnv("TEST");
                    request.setName(nombre.getText().toString());
                    request.setLastname(apellido.getText().toString());
                    request.setDni(Long.parseLong(dni.getText().toString()));
                    request.setEmail(email.getText().toString());
                    request.setPassword(password.getText().toString());
                    request.setComission(Long.parseLong(comision.getText().toString()));
                    request.setGroup(Long.parseLong(grupo.getText().toString()));

                    Retrofit retrofit = new Retrofit.Builder() //hay que agregar las dependencias en build.gradle
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl("http://so-unlam.net.ar/api/")
                            .build();

                    Service service = retrofit.create(Service.class);

                    Call<ResponseRegistro> call = service.register(request);
                    call.enqueue(new Callback<ResponseRegistro>(){ //ejecuccion asincrónica, si fuera sincronica se queda esperando la respuesta
                        @Override
                        public void onResponse(Call<ResponseRegistro> call, Response<ResponseRegistro> response){

                            if(response.isSuccessful()){
                                /*Acá se puede mostrar un msj que se registró con exito*/
                                /*tambien hay que guardar los datos que nos devuelve y pasar al menu principal*/
                                /*Los datos se optienen como "token = response.body().getToken()"*/
                                Toast.makeText(getApplicationContext(),"Registro Exitoso",Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(),"Usuario:" + request.getName(),Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(),"Token" + response.body().getToken(),Toast.LENGTH_LONG).show();
                            }else{
                                Log.e("Fallo", response.toString());
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseRegistro> call, Throwable t){ //Cuando hay una mala configuracion del retrofit
                            Log.e("Fallo", t.getMessage());
                        }

                    });
                }
            }
        });
    }

    public boolean validarCampos(){
        String campNombre = nombre.getText().toString();
        String campApellido = apellido.getText().toString();
        String campDni = dni.getText().toString();
        String campEmail = email.getText().toString();
        String campPass = password.getText().toString();
        String campComi = comision.getText().toString();

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

        return valido;
    }

}
