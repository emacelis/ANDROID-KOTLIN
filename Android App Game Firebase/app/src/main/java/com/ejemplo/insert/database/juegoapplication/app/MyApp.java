package com.ejemplo.insert.database.juegoapplication.app;

import android.app.Application;

import com.google.firebase.FirebaseApp;
//metodo que inicia de inicio con la app...recordar name app..MANIFEST
public class MyApp extends Application {

    public static  MyApp instance;

    public static MyApp getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {

        instance=this;

        super.onCreate();
        //para evitar iniciar firebase..
        FirebaseApp.initializeApp(this);
    }
}
