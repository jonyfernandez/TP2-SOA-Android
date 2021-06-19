package com.example.myapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FormularioTest extends AppCompatActivity implements SensorEventListener {

    private EditText temperatura;
    private CheckBox olfato, gusto, tos, respiracion, cabeza, contactoEstrecho, musculares;
    private List<CheckBox> sintomas = new ArrayList<>();

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

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void diagnosticar(View view) {

        for(CheckBox sintoma : sintomas){
            if(sintoma.isChecked()){

            }
        }


    }

    public void volverAlMenu(View view) {

        Intent intent = new Intent(FormularioTest.this, MenuPrincipal.class);
        startActivity(intent);
    }
}
