package com.ejemplo.insert.database.oauth2udemyretrofitjava.shared_preferences;

import android.content.SharedPreferences;

import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.Token;

public class TokenManager {
    //Token manager para poder recuperar y guardar los token

    public static final String SHARED_PREFERENCES= "SHARED_PREFERENCES";
    public static final String SHARED_PREFERENCES_ACCES_TOKEN="SHARED_PREFERENCES_ACCES_TOKEN";
    public static final String SHARED_PREFERENCES_REFRESH_TOKEN="SHARED_PREFERENCES_REFRESH_TOKEN";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE=null;

    private TokenManager(SharedPreferences sharedPreferences){
        this.sharedPreferences=sharedPreferences;
        this.editor=sharedPreferences.edit();
    }

    //SE SYNCRONIZA PARA QUE EL METODO NO SE REPITA!!!
    public static synchronized TokenManager getInstance(SharedPreferences sharedPreferences){
        if(INSTANCE==null){
            INSTANCE=new TokenManager(sharedPreferences);
        }return INSTANCE;
    }

    public Token getToken(){
        Token token=new Token();
        token.setAccesToken(sharedPreferences.getString(SHARED_PREFERENCES_ACCES_TOKEN,null));
        token.setRefreshToken(sharedPreferences.getString(SHARED_PREFERENCES_REFRESH_TOKEN,null));
        return token;
    }

    //METODOS
    //SALVAR TOKEN Y OBTENER TOKEN
    public void savedToken(Token token){
        //ES IMPORTANTE COLOCAR EL .commit() para que se guartde la operacion
        editor.putString(SHARED_PREFERENCES_ACCES_TOKEN,token.getAccesToken()).commit();
        editor.putString(SHARED_PREFERENCES_REFRESH_TOKEN,token.getRefreshToken()).commit();
    }


}

