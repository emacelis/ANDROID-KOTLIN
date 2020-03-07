package com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit;


import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {

    private static MiniTwitterClient instance=null;
    private MiniTwitterService miniTwitterService;
    private static HttpLoggingInterceptor loggingInterceptor;
    private Retrofit retrofit;
    private OkHttpClient.Builder httpClientBuilder;

    public MiniTwitterClient(){
        httpClientBuilder=new OkHttpClient.Builder();
        loggingInterceptor=new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder=new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        retrofit=new Retrofit.Builder()
                .baseUrl(Constantes.API_WEBSERVICERETROFIT_BASE_URL)
                .client(httpClientBuilder.build())//okhttp3
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Otra forma de aplicar Forsquare
        miniTwitterService=retrofit.create(MiniTwitterService.class);
    }

    //SINGELTON la INSTANCIA SOLO SE CREA UNA SOLA VES
    public static synchronized MiniTwitterClient getInstance(){
        if(instance==null){
            instance=new MiniTwitterClient();
        }return instance;
    }


    //Otra forma de aplicar Forsquare

    public MiniTwitterService getMiniTwitterService(){
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
