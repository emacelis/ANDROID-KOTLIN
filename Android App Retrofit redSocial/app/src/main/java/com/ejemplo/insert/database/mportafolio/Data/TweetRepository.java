package com.ejemplo.insert.database.mportafolio.Data;


import android.widget.Toast;

import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Constantes.MyApp;
import com.ejemplo.insert.database.mportafolio.Constantes.SharedPreferencesManager;
import com.ejemplo.insert.database.mportafolio.pojomodel.Like;
import com.ejemplo.insert.database.mportafolio.pojomodel.RequestCreateTwitter;
import com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit.AuthTweetService;
import com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit.AuthTwitterClient;
import com.ejemplo.insert.database.mportafolio.pojomodel.Tweet;
import com.ejemplo.insert.database.mportafolio.pojomodel.TweetDeleted;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {
    AuthTweetService authTwiterService;
    AuthTwitterClient authTwitterClient;
    //LiveData<List<Tweet>> allTweets;//se maneja un LiveData..porque la lista es en vivo
    MutableLiveData<List<Tweet>> allTweets;//se cambia la variable debido a que se le añade un nuevo tweet
    //para traer los metodos que te daran el TW favorito
    MutableLiveData<List<Tweet>>favTweets;
    String username;

    public TweetRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwiterService = authTwitterClient.getAuthTwitterService();
        allTweets=getAllTweets();//si se modifica la laista de tweets le avisara a
        //  LiveData<List<Tweet>> para que eun OBSERVADOR ESTE AL PENDIENTE PARA ACTUALIZAR
        //Rescatar constantes
        username= SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME);
    }

    //GetAllTweets
    public MutableLiveData<List<Tweet>> getAllTweets() {
        //para poder tener todos los tweets
        //final MutableLiveData<List<Tweet>> data = new MutableLiveData<>();

        //para todoss los tweets
        if (allTweets==null){
            allTweets=new MutableLiveData<>();
        }
        Call<List<Tweet>> call = authTwiterService.getallTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    //asignar la lista de tweets,A la variable data se lñe asigan
                    allTweets.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();

            }
        });

        return allTweets;//Regresamos Data
    }

    //RECORRIDO DE LA LISTA DE FAV
    public MutableLiveData<List<Tweet>>getfavsTweets(){
        if(favTweets==null){
            favTweets=new MutableLiveData<>();
        }
        List<Tweet> newFavList=new ArrayList<>();
        Iterator iterator=allTweets.getValue().iterator();
        while (iterator.hasNext()) {
            Tweet current=(Tweet)iterator.next();
            Iterator itLikes=current.getLikes().iterator();
            boolean enc=false;
            while (itLikes.hasNext()&&!enc) {
                Like like = (Like) itLikes.next();
                if (like.getUsername().equals(username)) {
                    enc = true;
                    newFavList.add(current);
                }
            }
        }
        favTweets.setValue(newFavList);
        return favTweets;
    }

    //Crea mensaje
    public void createTweet(String mensaje){
        RequestCreateTwitter requestCreateTwitter= new RequestCreateTwitter(mensaje);
        Call<Tweet> call=authTwiterService.createTweet(requestCreateTwitter);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    //Se clona la lista de Tweets, y se le añade uno nuebvo ..ya que no se puede modificar toda la lista en si
                    List<Tweet>listaclonada=new ArrayList<>();
                    //Añadimos en prmier lougar el tweet de lserver
                    listaclonada.add(response.body());
                    for(int i=0;i<allTweets.getValue().size();i++){
                        listaclonada.add(new Tweet(allTweets.getValue().get(i)));
                    }
                    //se setea la lista creada
                    allTweets.setValue(listaclonada);
                    //ahora se llama al metodo desde TweeterVieModel

                    //se refresa la lista de tweetsFAVORITOS
                    getfavsTweets();
                    //ahora se tiene que ir a l Viewmodel para implementar o llamar los metodos

                }else{
                    Toast.makeText(MyApp.getContext(),"intentelo de nuevo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(),"Erro en la Conexión",Toast.LENGTH_SHORT).show();

            }
        });

    }


    //ELIMINAR
    public void deleteTweet(final int idTweet){
        Call<TweetDeleted> call =authTwiterService.deleteTweet(idTweet);
        call.enqueue(new Callback<TweetDeleted>() {
            @Override
            public void onResponse(Call<TweetDeleted> call, Response<TweetDeleted> response) {
                if(response.isSuccessful()){
                    //Se colona una lista de tweet excepto el que se acaba de eliminar
                    List<Tweet>cloneTweets =new ArrayList<>();
                    //Si la respuesta es correcta se elimina el archivo en la base de datos
                    //pero para no pedir de nuevo el metodo, se crea el clon de la lista
                    //y se recorre toda la lista omitiendo el tweet eliminado por id
                    for (int i=0;i<allTweets.getValue().size();i++){
                        if(allTweets.getValue().get(i).getId()!=idTweet){
                            cloneTweets.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    //se setea la lista creada
                    allTweets.setValue(cloneTweets);
                    //se refresaca con los que tienen like
                    getfavsTweets();
                }else{
                    Toast.makeText(MyApp.getContext(),"Error",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<TweetDeleted> call, Throwable t) {
                Toast.makeText(MyApp.getContext(),"Error en la Conexión",Toast.LENGTH_SHORT).show();
            }
        });
    }



    //LIKE TWEET
    public void likeTweet(final int idTweet){
        Call<Tweet> call=authTwiterService.likeTweet(idTweet);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    //Se clona la lista de Tweets, y se le añade uno nuebvo ..ya que no se puede modificar toda la lista en si
                    List<Tweet>listaclonada=new ArrayList<>();

                    for(int i=0;i<allTweets.getValue().size();i++){
                        if(allTweets.getValue().get(i).getId()==idTweet){
                            //Si se encontro el id con un igual al del like se
                            //se introduce el elemento que nos llega del servidor
                            listaclonada.add(response.body());
                            //!!AHORA EN EL VIEWMODEL SE COLOCA EL METODO QUE HACE REFERNCIA A ESTE PARA SER INVOCADO
                        }else{
                            listaclonada.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    //se setea la lista creada
                    allTweets.setValue(listaclonada);
                    //ahora se llama al metodo desde TweeterVieModel
                }else{
                    Toast.makeText(MyApp.getContext(),"intentelo de nuevo",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(),"Erro en la Conexión",Toast.LENGTH_SHORT).show();
            }
        });
    }



}
