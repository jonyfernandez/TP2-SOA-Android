package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Eventos extends AppCompatActivity {

    private TextView valor_temperatura1, valor_temperatura2;
    private TextView tipos_sintomas1, tipos_sintomas2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        valor_temperatura1 = (TextView) findViewById(R.id.etTemperatura1);
        valor_temperatura2 = (TextView) findViewById(R.id.etTemperatura2);
        tipos_sintomas1 = (TextView) findViewById(R.id.etSintomas1);
        tipos_sintomas2 = (TextView) findViewById(R.id.etSintomas2);

        SharedPreferences temperatura = getSharedPreferences("temperatura", Context.MODE_PRIVATE);
        String temperatura1 = temperatura.getString("temperatura1", "no hay valor");
        String temperatura2 = temperatura.getString("temperatura2", "no hay valor");

        valor_temperatura1.setText(temperatura1);
        valor_temperatura2.setText(temperatura2);

        SharedPreferences sintomas = getSharedPreferences("sintomas", Context.MODE_PRIVATE);
        String sintomas1 = sintomas.getString("sintomas1", "no hay valor");
        String sintomas2 = sintomas.getString("sintomas2", "no hay valor");

        tipos_sintomas1.setText(sintomas1);
        tipos_sintomas2.setText(sintomas2);

    }

    public void volverAlMenu(View view) {
        Intent intent = new Intent(Eventos.this, MenuPrincipal.class);
        startActivity(intent);
    }
}
