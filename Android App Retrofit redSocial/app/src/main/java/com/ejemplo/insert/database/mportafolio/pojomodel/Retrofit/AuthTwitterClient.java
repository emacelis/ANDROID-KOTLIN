package com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit;


import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {

    private static AuthTwitterClient instance=null;
    private AuthTweetService miniTwitterService;
    private static HttpLoggingInterceptor loggingInterceptor;
    private Retrofit retrofit;
    private OkHttpClient.Builder httpClientBuilder;
    private OkHttpClient cliente;

    public AuthTwitterClient(){
        httpClientBuilder=new OkHttpClient.Builder();
        loggingInterceptor=new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder=new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        httpClientBuilder.addInterceptor(new AuhtInterceptor());

        cliente=httpClientBuilder.build();


        retrofit=new Retrofit.Builder()
                .baseUrl(Constantes.API_WEBSERVICERETROFIT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(cliente)//okhttp3 httpClientBuilder.build() para instanciar la clase m,aster de Retrofit
                .build();
        //Otra forma de aplicar Forsquare
        miniTwitterService=retrofit.create(AuthTweetService.class);
    }

    //SINGELTON la INSTANCIA SOLO SE CREA UNA SOLA VES
    public static synchronized AuthTwitterClient getInstance(){
        if(instance==null){
            instance=new AuthTwitterClient();
        }return instance;
    }


    //Otra forma de aplicar Forsquare

    public AuthTweetService getAuthTwitterService(){
        return miniTwitterService;
    }


    //Consumir de nuestra api
    //clase
    /*
   public <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }
*/
}


