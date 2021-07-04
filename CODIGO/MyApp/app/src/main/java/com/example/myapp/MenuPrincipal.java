package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    public void irATestCOVID(View view) {
        Intent intent = new Intent(MenuPrincipal.this, FormularioTest.class);
        startActivity(intent);
    }

    public void irARecordatorio(View view) {
        Toast.makeText(getApplicationContext(), "PRÓXIMAMENTE...", Toast.LENGTH_LONG).show();
    }

    public void irAListaEventos(View view) {
        Intent intent = new Intent(MenuPrincipal.this, Eventos.class);
        startActivity(intent);
    }
}
