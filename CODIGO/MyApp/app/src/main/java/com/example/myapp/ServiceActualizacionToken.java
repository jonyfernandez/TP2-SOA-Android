package com.example.myapp;

import android.app.IntentService;
import android.content.Intent;

import static java.lang.Thread.sleep;

public class ServiceActualizacionToken extends IntentService {

    private static boolean ejecutando;
    private int tInicio, tFin;
    private static int FIN_TIEMPO = 360000; //6 minutos

    public ServiceActualizacionToken() {
        super("ServiceActualizacionToken");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        tInicio = (int) System.currentTimeMillis();
        while(ejecutando){
            tFin = (int) System.currentTimeMillis() - tInicio;
            if(tFin > FIN_TIEMPO){
                PedidoAPI actualizacionToken = new PedidoAPI();
                actualizacionToken.actualizarToken();
                tInicio = (int) System.currentTimeMillis();
            }
            try{
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void iniciarTimer(){
        ejecutando = true;
    }

    public static void detenerTimer(){
        ejecutando = false;
    }

}
