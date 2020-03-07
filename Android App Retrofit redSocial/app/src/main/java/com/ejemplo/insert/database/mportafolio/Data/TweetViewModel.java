package com.ejemplo.insert.database.mportafolio.Data;

import android.app.Application;
import android.content.Context;

import com.ejemplo.insert.database.mportafolio.Ui.tweets.BottonModalTweetFragment;
import com.ejemplo.insert.database.mportafolio.pojomodel.Tweet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TweetViewModel extends AndroidViewModel {
    //Extende de Andrtoid ViewModel que viende andoridJetPack
    //definir un acceso al web service ..reposutoerio
    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favtweets;



    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository=new TweetRepository();
        tweets=tweetRepository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets(){return tweets;}

    public LiveData<List<Tweet>> getFavTweets(){
        favtweets=tweetRepository.getfavsTweets();
        return favtweets;
    }

    public LiveData<List<Tweet>> getNewTweets(){
        tweets=tweetRepository.getAllTweets();
        return tweets;
    }

    public LiveData<List<Tweet>> getNewFAVTweets(){
        getNewTweets();
        return getFavTweets();
    }

    public void insertTweet(String mensaje){
        tweetRepository.createTweet(mensaje);
    }

    //LIKE CORAZON
    public void likeTweet(int idTweet){
        tweetRepository.likeTweet(idTweet);
        //!! AHORA EN EL ADAPTER SE TIENE QUE IDENTIFICAR CUANDO SE LE DA LIKE O NO AL CORAZON
    }


    //ELIMINAR
    public void deleteTweet(int idTweet){
        tweetRepository.deleteTweet(idTweet);
    }

    public void openDialogTweetMenu(Context ctx, int idTweet){
        BottonModalTweetFragment dialogTweet=BottonModalTweetFragment.newInstance(idTweet);
        //se castea el objeto a activity porque si no no se puede pasar
        dialogTweet.show(((AppCompatActivity)ctx).getSupportFragmentManager()
                ,"BottonModalTweetFragment");
    }
}
