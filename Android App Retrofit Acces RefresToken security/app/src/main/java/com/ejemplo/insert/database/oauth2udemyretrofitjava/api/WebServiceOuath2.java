package com.ejemplo.insert.database.oauth2udemyretrofitjava.api;

import com.ejemplo.insert.database.oauth2udemyretrofitjava.shared_preferences.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceOuath2 {
    private static final String BASE_URL="http://10.0.2.2:8071";

    private static HttpLoggingInterceptor loggingInterceptor;
    private Retrofit retrofit;
    private OkHttpClient.Builder httpClientBuilder;
    private static WebServiceOuath2 instance;

    private WebServiceOuath2(){
        httpClientBuilder=new OkHttpClient.Builder();
        loggingInterceptor=new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder=new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized WebServiceOuath2 getInstance(){
        if(instance==null){
            instance=new WebServiceOuath2();
        }return instance;
    }

    //clase generica
    public <S> S createService(Class<S> serviceClass){
    return retrofit.create(serviceClass);
    }

    //CREANDO SEGUNDO SERVICIO DE FORMA SEGURA OAUTH2
    //CREAMOS OTRO SERVICIO PARA EL REFRESH_TOKEN
    public <S> S createServiceWhithOauth2(Class <S> serviceClass,final TokenManager tokenManager){
        final OkHttpClient newClient=httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //request original
                Request requestOriginal=chain.request();
                Request.Builder builder =requestOriginal.newBuilder();
        //autorizaciopn a travez del header
                if(tokenManager.getToken().getAccesToken()!=null){
                    builder.addHeader("Authorization","Bearer "+tokenManager.getToken().getAccesToken());
                }
                Request request=builder.build();
                return chain.proceed(request);
            }
        }).authenticator(CustomAuthenticator.getInstance(tokenManager)).build();
        //autentificacion del token
        return retrofit.newBuilder().client(newClient).build().create(serviceClass);
    }

}
