package com.ejemplo.insert.database.mportafolio.Constantes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String APP_SETINGS_FILE="APP_SETINGS_FILE";

    //metodo que hacer referencias a las funciones de esta clase
    private SharedPreferencesManager(){}

    //SharedPreferences de la INTERFAS!!!!!OBJETO
    private static SharedPreferences getSharedPreferences(){
        return  MyApp.getContext().getSharedPreferences(APP_SETINGS_FILE, Context.MODE_PRIVATE);
    }

    public static void setSomeStringvalue(String dataLabel,String daraValue){
        SharedPreferences.Editor editor=getSharedPreferences().edit();
        editor.putString(dataLabel,daraValue);
        editor.commit();
    }
    public static void setSomeBooleanvalue(String dataLabel,boolean daraValue){
        SharedPreferences.Editor editor=getSharedPreferences().edit();
        editor.putBoolean(dataLabel,daraValue);
        editor.commit();
    }

    //METODOS QUE GUARDEN EL TOKEN
    public static String getSomeStringValue(String dataLabel){
        return getSharedPreferences().getString(dataLabel,null);
    }
    public static boolean getSomeBooleanValue(String dataLabel){
        return getSharedPreferences().getBoolean(dataLabel,false);
    }
}
