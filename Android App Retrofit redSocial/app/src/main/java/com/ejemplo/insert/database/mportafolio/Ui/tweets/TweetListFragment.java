package com.ejemplo.insert.database.mportafolio.Ui.tweets;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Data.TweetViewModel;
import com.ejemplo.insert.database.mportafolio.R;

import com.ejemplo.insert.database.mportafolio.pojomodel.Tweet;

import java.util.List;


public class TweetListFragment extends Fragment {


    private int tweetListType = 1;
    private RecyclerView recyclerView;
    private MyItemRecyclerViewAdapter adapter;
    List<Tweet>tweetList;

    TweetViewModel tweetViewModel;
    //SWIPER REFRESH LAYOUT
    SwipeRefreshLayout swipeRefreshLayout;


    public TweetListFragment() {

    }

    public static TweetListFragment newInstance(int tweetListType) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(Constantes.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //de la libreria externa lifecycle.ViewModelProviders;
        tweetViewModel= ViewModelProviders.of(getActivity())
                .get(TweetViewModel.class);

        if (getArguments() != null) {
            tweetListType = getArguments().getInt(Constantes.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        //  if (view instanceof RecyclerView) {
        Context context = view.getContext();
        //SWIPER REFRESH LAYOUT
        recyclerView=view.findViewById(R.id.list);
        swipeRefreshLayout=view.findViewById(R.id.swiperefreshlayout);
        //para obtener la referencia a un color es getResources().getcolor((elcolor))
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAzul));

        //metodo que se va a lanzar cuando se haga el refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);//se activa el refresh

                //Observacion, se esta llamando a un observer que es un modelo del TweetViewModel
                //y ese metodo regresa la lista de tweets..que es la misma..para eso se crea otro metodod independiente
                //que carge los tweets nuevos..getNewTweets

                if(tweetListType==Constantes.TWEET_LIST_ALL){
                    loadNewTweetData();                    }
                else if(tweetListType==Constantes.TWEET_LIST_FAVS){
                    loadNewFabTweetData();
                }

            }
        });

        //HACER REFERECNIA AL LIST SI NO NO LO VA A ENCONTRAR"!!!
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        //SE QUITAN YA QUE NO NECSITA..solo la condicion vertical
        //   if (mColumnCount <= 1) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //  } else {
        // recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        // }
        //SE COLOCA POR EL OBSERVER
        adapter =new MyItemRecyclerViewAdapter(
                getActivity(),
                tweetList
                //la respuesta del servidor
        );
        recyclerView.setAdapter(adapter);

//PARA VER SOLO LOS  QUE TE GUSTAN
        if(tweetListType==Constantes.TWEET_LIST_ALL){
            loadTweetData();
        }
        else if(tweetListType== Constantes.TWEET_LIST_FAVS){
            loadFabTweetData();
        }

        //}
        return view;
    }

    private void loadNewFabTweetData() {
        tweetViewModel.getNewFAVTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList=tweets;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getNewFAVTweets().removeObserver(this);//para que no choquen dos observadores
            }
        });

    }

    private void loadFabTweetData() {
        tweetViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList=tweets;
                adapter.setData(tweetList);
            }
        });
    }


    private void loadTweetData(){
        //Se carga la funcion creada e tweetViewmodel
        //cuando recibamos que ha ccurrido un cambio por medio del Observer-onchange
        //tweetViewModel se conecta a Repositorio-API
        tweetViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList=tweets;
                adapter.setData(tweetList);
            }
        });

    }
    //MEtodo de Refresh
    private void loadNewTweetData() {
        tweetViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                //swipeRefresh..para finalizar la tarea del refresh
                swipeRefreshLayout.setRefreshing(false);
                //luegop tenemos que decirl a l aapter que se va  recibir una nueva lista de tweets..en MyTwetRecyclerView
                //se invoca con
                adapter.setData(tweetList);
                //!!!
                //PARA QUE DOS OBSERVADORES loadNewTweetData y loadTweetData  NO CHOQUEN
                //A LA HORA DE INSETAR UN TWEET se DETIENE UNO CUANDO TERMINA SU EJECUCION Y LA FORMA DE HACERLO ES:
                tweetViewModel.getNewTweets().removeObserver(this);
                //con esto se elimina este observador
            }
        });
    }
}
