package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FormularioTest extends AppCompatActivity implements SensorEventListener {

    private EditText temperatura;
    private CheckBox olfato, gusto, tos, respiracion, cabeza, contactoEstrecho, musculares;
    private double valorTemperatura;
    private SensorManager sensorManager;
    private Sensor acelerometro;
    private Sensor proximidad;
    private DecimalFormat unDecimal = new DecimalFormat("###.#");
    private SharedPreferences sen_proximidad, tipos_sintomas;
    private int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        temperatura = findViewById(R.id.etTemperatura);
        olfato = findViewById(R.id.checkOlfato);
        gusto = findViewById(R.id.checkGusto);
        tos = findViewById(R.id.checkTos);
        respiracion = findViewById(R.id.checkRespiratoria);
        cabeza = findViewById(R.id.checkCabeza);
        contactoEstrecho = findViewById(R.id.checkEstrecho);
        musculares = findViewById(R.id.checkMusculares);

        SharedPreferences contar = getSharedPreferences("contador", Context.MODE_PRIVATE);
        contador = contar.getInt("contador", 0);

    }

    private void registrarSensores() {
        sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void quitarSensores() {
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registrarSensores();
    }


    @Override
    protected void onDestroy() {
        quitarSensores();
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        synchronized (this) {

            switch (event.sensor.getType()) {

                case Sensor.TYPE_ACCELEROMETER:
                    Log.d("Acelerometro", String.valueOf(event.values[0]));

                    if (event.values[0] > 8.5 || event.values[0] < -8.5)
                        Toast.makeText(getApplicationContext(), "Celular en Horizontal", Toast.LENGTH_LONG).show();

                    break;

                case Sensor.TYPE_PROXIMITY:
                    Log.d("Proximidad", String.valueOf(event.values[0]));
                    //tomamos un valor y dejamos de escuchar
                    sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));

                    if(event.values[0] == 0){ //Cuando lo detecta tira un random para simular el sensor de temperatura.
                        double valorTemp = (Math.random()*(40 - 36) + 36); //Temperatura entre 36 y 39
                        temperatura.setText(unDecimal.format(valorTemp));
                    }

                    break;
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void diagnosticar(View view) {

        String sintomasConfirmados = "";
        boolean covid = false;
        boolean tFiebre = false;
        boolean tOlfato = false;
        boolean tGusto = false;
        boolean tRespiracion = false;
        int cantidadSintomas = 0;

        String valorTem = temperatura.getText().toString().replace(",",".");
        if(!valorTem.isEmpty()) {
            valorTemperatura = Double.parseDouble(valorTem);

            Intent intent = new Intent(FormularioTest.this, Diagnostico.class);
            intent.putExtra("temperatura", valorTem);

            if (valorTemperatura > 38) {
                tFiebre = true;
                sintomasConfirmados += "Tiene temperatura alta\n";
            }

            if (olfato.isChecked()) {
                cantidadSintomas++;
                tOlfato = true;
                sintomasConfirmados += "Sin olfato\n";
            }

            if (gusto.isChecked()) {
                tGusto = true;
                sintomasConfirmados += "Sin gusto\n";
            }

            if (tos.isChecked()) {
                cantidadSintomas++;
                sintomasConfirmados += "Tiene tos\n";
            }

            if (respiracion.isChecked()) {
                cantidadSintomas++;
                tRespiracion = true;
                sintomasConfirmados += "Tiene dificultad respiratoria\n";
            }

            if (cabeza.isChecked()) {
                cantidadSintomas++;
                sintomasConfirmados += "Tiene dolores de cabeza\n";
            }

            if (contactoEstrecho.isChecked()) {
                cantidadSintomas++;
                sintomasConfirmados += "Tuvo contacto estrecho\n";
            }

            if (musculares.isChecked()) {
                cantidadSintomas++;
                sintomasConfirmados += "Tiene dolores musculares\n";
            }

            if (tFiebre == true && tOlfato == true && tGusto == true && tRespiracion == true) {
                covid = true;
            }else if (tOlfato == true && tGusto == true && tRespiracion == true) {
                covid = true;
            }else if (tOlfato == true && tGusto == true) {
                covid = true;
            }

            contador++;
            if(contador > 2)
                contador = 0;

            guardarPreferencias(sen_proximidad, valorTem, true, contador);
            guardarPreferencias(tipos_sintomas, sintomasConfirmados, false, contador);

            SharedPreferences contar = getSharedPreferences("contador", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = contar.edit();
            editor.putInt("contador", contador);
            editor.commit();

            intent.putExtra("covid", covid);
            intent.putExtra("cantidad_sintomas", cantidadSintomas);
            intent.putExtra("sintomas", sintomasConfirmados);

            startActivity(intent);
        }else
            Toast.makeText(getApplicationContext(),"Debe tomarse la temperatura", Toast.LENGTH_LONG).show();

    }

    /**
     * Metodo donde guardo eventos de temperatura y sintomas
     * y lo muestro en la lista de eventos
     *
     * */

    private void guardarPreferencias(SharedPreferences preferencias, String valor, boolean proximidad, int n) {

        if(n == 0)
            n = 1;

        if(proximidad) {
            preferencias = getSharedPreferences("temperatura", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            String v = valor + " °C";
            editor.putString("temperatura" + n, v);
            editor.commit();
        }else{
            preferencias = getSharedPreferences("sintomas", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("sintomas" + n, valor);
            editor.commit();
        }

    }

    public void tomarTemperatura(View view) {
        sensorManager.registerListener(this, proximidad, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void volverAlMenu(View view) {

        Intent intent = new Intent(FormularioTest.this, MenuPrincipal.class);
        startActivity(intent);
    }

}
