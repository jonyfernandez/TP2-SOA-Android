package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static String CODIGO_VALIDACION = "73910";
    private EditText numeroTel;
    private EditText codigo;
    private Button boton_enviar;
    private Button boton_validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv ;
        tv = (TextView)findViewById(R.id.etBateria);
        numeroTel = findViewById(R.id.etNumeroTel);
        codigo = findViewById(R.id.etCodigo);
        boton_enviar = findViewById(R.id.btnEnviar);
        boton_validar = findViewById(R.id.btnValidar);
        codigo.setEnabled(false);
        boton_validar.setEnabled(false);

        IntentFilter intenfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, intenfilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float bateria = (level / (float)scale)*100;

        tv.setText("Nivel de bateria: " + (int)bateria + "%"); //Muestro el nivel de bateria.

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS},1);
        }

    }

    public void enviarMensaje(View view) {

        try{
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numeroTel.getText().toString(), null, CODIGO_VALIDACION, null, null);
            Toast.makeText(getApplicationContext(), "Codigo enviado", Toast.LENGTH_LONG).show();
            numeroTel.setEnabled(false);
            boton_enviar.setEnabled(false);
            codigo.setEnabled(true);
            boton_validar.setEnabled(true);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Msj no enviado", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Vuelva a intentarlo", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void validarCodigo(View view) {
        String codigoIngresado = codigo.getText().toString();
        if(codigoIngresado.equals(CODIGO_VALIDACION)){
            Toast.makeText(getApplicationContext(), "Codigo valido", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Codigo invalido." + " Por favor ingreselo nuevamente", Toast.LENGTH_LONG).show();
        }
    }
}
