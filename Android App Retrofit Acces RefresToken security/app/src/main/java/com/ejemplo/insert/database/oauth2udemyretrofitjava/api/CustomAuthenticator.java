package com.ejemplo.insert.database.oauth2udemyretrofitjava.api;

import android.util.Base64;

import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.Token;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.shared_preferences.TokenManager;

import java.io.IOException;

import androidx.annotation.Nullable;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

public class CustomAuthenticator implements Authenticator {
    //la clase extiende de Authenticator de okhttp3
    //implementas los metodos

    private TokenManager tokenManager;//para acceder a el token
    private static CustomAuthenticator INSTANCE;
    //patron Singelton
    private CustomAuthenticator(TokenManager tokenManager){
        this.tokenManager=tokenManager;
    }
    //metodo para obtener la instancia
    public static synchronized  CustomAuthenticator getInstance(TokenManager tokenManager){
        if(INSTANCE==null){
            INSTANCE=new CustomAuthenticator(tokenManager);
        }return INSTANCE;
    }

//Se ejecuta cuando nuestro token de accesos se ha caducado ..en caso de que el token de refesco ha caduicado tienes que ingresar
    //de nuevo a la pp
    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {
        //token de acceso
        String authHeader="Bearer "+ Base64.encodeToString(("androidApp:123").getBytes(),Base64.NO_WRAP);
        //token de refresco
        Token token=tokenManager.getToken();

        //hacer la llamada para detectar si ha EXPIRADO
        //call de retrofit
        Call<Token> call=WebServiceOuath2
                .getInstance()
                .createService(WebServiceOuathApi.class)
                .obtenerTokenconRefreshToken(
                        authHeader,
                        token.getRefreshToken(),
                        "refresh_token"//meterlo en constantes
                );


        retrofit2.Response<Token> response1=call.execute();
        if (response.isSuccessful()){
            //actualizar el token de acceso
            Token newToken=response1.body();
            //guardar el token
            tokenManager.savedToken(newToken);
            return response.request().newBuilder().header("Authorization","Bearer "
                    +response1.body().getAccesToken()).build();
        }else{
            return null;
        }
    }
}
