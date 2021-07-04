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
        Boolean covid = extras.getBoolean("covid");
        int cantidadSintomas = extras.getInt("catidad_sintomas");
        String sintomasConfirmados = extras.getString("sintomas");
        sintomas.setText(sintomasConfirmados);

        if(sintomasConfirmados.isEmpty()){
            sintomas.setText("Usted no posee sintomas relevantes");
            resultado.setText("Por tal motivo usted no tiene Covid");
        }

        if(covid){
            resultado.setText("Usted es positivo de Covid-19\nPara una mejor confirmacion, realicese hisopado");
        }else if(cantidadSintomas >= 2) {
            resultado.setText("Usted puede ser posible positivo de Covid-19\nPara una mejor confirmacion, realicese un hisopado");
        }else
            resultado.setText("Usted no tiene Covid-19\nSiga cuidandose");
    }

    public void volverAlMenu(View view) {
        Intent intent = new Intent(Diagnostico.this, MenuPrincipal.class);
        startActivity(intent);
    }
}
