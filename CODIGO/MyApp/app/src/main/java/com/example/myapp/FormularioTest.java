package com.example.myapp;

import android.content.Intent;
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
    private List<CheckBox> sintomas = new ArrayList<>();
    private double valorTemperatura;
    private SensorManager sensorManager;
    private DecimalFormat unDecimal = new DecimalFormat("###.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        temperatura = findViewById(R.id.etTemperatura);
        olfato = findViewById(R.id.checkOlfato);
        gusto = findViewById(R.id.checkGusto);
        tos = findViewById(R.id.checkTos);
        respiracion = findViewById(R.id.checkRespiratoria);
        cabeza = findViewById(R.id.checkCabeza);
        contactoEstrecho = findViewById(R.id.checkEstrecho);
        musculares = findViewById(R.id.checkMusculares);
        sintomas.add(olfato);
        sintomas.add(gusto);
        sintomas.add(tos);
        sintomas.add(respiracion);
        sintomas.add(cabeza);
        sintomas.add(contactoEstrecho);
        sintomas.add(musculares);
    }

    private void registrarSensores() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
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
            Log.d("Acelerometro", String.valueOf(event.values[0]));

            switch (event.sensor.getType()) {

                case Sensor.TYPE_ACCELEROMETER:

                    if (event.values[0] > 9.5 || event.values[0] < -9.5)
                        Toast.makeText(getApplicationContext(), "Celular en horizontal", Toast.LENGTH_SHORT).show();

                    break;

                case Sensor.TYPE_PROXIMITY:
                    sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
                    temperatura.setText(unDecimal.format(event.values[0]));

                    break;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void diagnosticar(View view) {

        String sintomasConfirmados = "";
        valorTemperatura = Double.parseDouble(temperatura.getText().toString().replace(",","."));

        Intent intent = new Intent(FormularioTest.this, Diagnostico.class);
        intent.putExtra("temperatura", temperatura.getText().toString().replace(",","."));

        if(valorTemperatura > 38.5){
            sintomasConfirmados += "Tiene temperatura alta\n";
        }

        if(olfato.isChecked()){
            sintomasConfirmados += "Sin olfato\n";
        }

        if(gusto.isChecked()){
            sintomasConfirmados += "Sin gusto\n";
        }

        if(tos.isChecked()){
            sintomasConfirmados += "Tiene tos\n";
        }

        if(respiracion.isChecked()){
            sintomasConfirmados += "Tiene dificultad respiratoria\n";
        }

        if(cabeza.isChecked()){
            sintomasConfirmados += "Tiene dolores de cabeza\n";
        }

        if(contactoEstrecho.isChecked()){
            sintomasConfirmados += "Tuvo contacto estrecho\n";
        }

        if(musculares.isChecked()){
            sintomasConfirmados += "Tiene dolores musculares\n";
        }


    }

    public void TomarTemperatura(View view) {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void volverAlMenu(View view) {

        Intent intent = new Intent(FormularioTest.this, MenuPrincipal.class);
        startActivity(intent);
    }

}
