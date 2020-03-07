package com.ejemplo.insert.database.mportafolio.Ui.tweets;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Constantes.SharedPreferencesManager;
import com.ejemplo.insert.database.mportafolio.Data.TweetViewModel;
import com.ejemplo.insert.database.mportafolio.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class NuevoTweetDialogFragment extends DialogFragment implements View.OnClickListener {

    ImageView ivClose,ivAvatar;
    Button btnTwittear;
    EditText etMensaje;
    //Creamos un oncreate para los componentes principales
    //Dialog dialogNuevoTweet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Con esto personalizamos el estilo del fragemnt en la pantalla
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogSyle);

        //para evitar errores con el dialog
        //dialogNuevoTweet=getDialog();
    }

    //Cargar vista de fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //Se super pone toda una vista ..por eso el false en el view
        View view=inflater.inflate(R.layout.nuevo_tweet_full_dialog,container,false);

        //
        ivClose=view.findViewById(R.id.imageViewClose);
        ivAvatar=view.findViewById(R.id.imageViewAvatar);
        btnTwittear=view.findViewById(R.id.buttonEcho);
        etMensaje=view.findViewById(R.id.editTextMensaje);

        //Eventos
        btnTwittear.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        //Seteamos la imagen de perfil
        String  photoUrl= SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL);
        //SE CARGA LA IMAGEN
        if (!photoUrl.isEmpty()){
            Glide.with(getActivity())
                    .load(Constantes.API_MINITWITTER_FILES_URL+photoUrl)
                    .into(ivAvatar);
        }


        return view;
    }

    //se sobre esctib e l clik..en este caso para los botones y mensase
    @Override
    public void onClick(View v) {
        int id=v.getId();
        String mensaje=etMensaje.getText().toString();

        if(id== R.id.buttonEcho) {
            if (mensaje.isEmpty()){
                Toast.makeText(getActivity(),"No dejar vacio el mensaje",Toast.LENGTH_SHORT).show();
            }else{
                TweetViewModel tweetViewModel= ViewModelProviders.of(getActivity())
                        .get(TweetViewModel.class);//apartir del modelo se inserta el mensaje
                tweetViewModel.insertTweet(mensaje);
                getDialog().dismiss();//para cerrar dialofof
            }

        }else if(id== R.id.imageViewClose){
            //checamos si el dialogo no es vaio
            if(!mensaje.isEmpty()){
                showDialogConfirm();
            }
            else{
                getDialog().dismiss();
            }
        }
    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Desa cancelar el mensaje?")
                .setTitle("Cancelar mensaje");

        //SE CARGAN LOS METODOS DE ANDORID PARA DIALOGFOS DE TEXTO
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //getdialog para obtener los tweets actyualizados junto con el nuestro
                getDialog().dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//SE CREA UN DIALOG PARA EVITAR ERRORES
            }
        });

        AlertDialog dialog=builder.create();
        //mostrar el dialofgo de confirmacion si no no aparce el tweet
        dialog.show();
    }
}
