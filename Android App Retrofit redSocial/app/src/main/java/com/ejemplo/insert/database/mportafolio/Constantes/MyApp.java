package com.ejemplo.insert.database.mportafolio.Constantes;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance(){
        return instance;
    }
    public static Context getContext(){
        return instance;
    }
    //instancia que solo se ejecuta una sola vez
    @Override
    public void onCreate(){
        // android:name=".Constantes.MyApp"
        instance=this;//solo se crea una sola vez .. ir a manifiesto para especificar que este va a gestionr el onjeto application
        super.onCreate();
    }
}
