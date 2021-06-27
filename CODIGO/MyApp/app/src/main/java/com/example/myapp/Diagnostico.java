package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Diagnostico extends AppCompatActivity {

    private TextView temperatura;
    private TextView sintomas;
    private TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico);

        temperatura = findViewById(R.id.etTemperatura);
        sintomas = findViewById(R.id.etSintomas);
        resultado = findViewById(R.id.etResultado);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        temperatura.setText("Su temperatura es: " + extras.getString("temperatura") + " °C");
        sintomas.setText(extras.getString("sintomas"));
        Boolean valorResultado = extras.getBoolean("resultado");

    }

    public void volverAlMenu(View view) {
        Intent intent = new Intent(Diagnostico.this, MenuPrincipal.class);
        startActivity(intent);
    }
}
