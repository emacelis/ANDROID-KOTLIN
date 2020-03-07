package com.ejemplo.insert.database.mportafolio.Ui.tweets;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Data.TweetViewModel;
import com.ejemplo.insert.database.mportafolio.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

public class BottonModalTweetFragment extends BottomSheetDialogFragment {

    private TweetViewModel mViewModel;
    //ELIMINAR
    private int idTweeteliminar;

    //SE MODIFICA EL METODO PARA TRER A LLAMAR EL TWEET A ELEMINAR
    public static BottonModalTweetFragment newInstance(int idTweet) {

        BottonModalTweetFragment fragment=new BottonModalTweetFragment();
        Bundle args=new Bundle();
        args.putInt(Constantes.ARG_TWEET_ID,idTweet);
        //!!!!IMPORTANTE PASAR LOS ARGUMENTOS AL FRTAGMENTO SI NO NO PASA NADA
        fragment.setArguments(args);
        return fragment;
    }

    //EL metodo on create nos premite rescatar los get argmunets que llegan a este metodo
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idTweeteliminar=getArguments().getInt(Constantes.ARG_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //GESTIONAR LOS ELEMENTOS DEL MENU] CON RETURN V
        View v= inflater.inflate(R.layout.botton_modal_tweet_fragment, container, false);

        final NavigationView nav=v.findViewById(R.id.navigatioon_view_button_tweet);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();

                if(id==R.id.action_delete_tweet){
                    mViewModel.deleteTweet(idTweeteliminar);
                    getDialog().dismiss();
                    return true;
                }

                return false;
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //colocar el get activity porque si no no reconoce esta actividad
        mViewModel = ViewModelProviders.of(getActivity()).get(TweetViewModel.class);
        // TODO: Use the ViewModel
    }

}
