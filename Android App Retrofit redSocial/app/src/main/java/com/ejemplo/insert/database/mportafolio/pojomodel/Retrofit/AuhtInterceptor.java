package com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit;


import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Constantes.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuhtInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
//ESTA CLASE CONCATERNARA LO QUE SE VA A ENVIAR A LSERVIDOR Y LE AÑADE LA CABEZERA CON EL TOKEN
        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);
        Request request=chain.request().newBuilder().addHeader("Authorization","Bearer "+token).build();
        return chain.proceed(request);    }
}
