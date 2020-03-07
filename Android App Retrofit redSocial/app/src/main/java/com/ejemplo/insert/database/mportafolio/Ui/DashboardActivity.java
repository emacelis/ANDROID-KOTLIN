package com.ejemplo.insert.database.mportafolio.Ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Constantes.SharedPreferencesManager;
import com.ejemplo.insert.database.mportafolio.Data.ProfileViewModel;
import com.ejemplo.insert.database.mportafolio.R;
import com.ejemplo.insert.database.mportafolio.Ui.Profile.ProfileFragment;
import com.ejemplo.insert.database.mportafolio.Ui.tweets.NuevoTweetDialogFragment;
import com.ejemplo.insert.database.mportafolio.Ui.tweets.TweetListFragment;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseUserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


public class DashboardActivity extends AppCompatActivity implements PermissionListener {
    FloatingActionButton fab,perfButton;
    ImageView ivAvatar;
    ProfileViewModel profileViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();

        //Se istancia el profileviewmodel
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);


        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment f = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        f = TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL);
                        fab.show();
                        break;
                    case R.id.navigation_dashboard:
                        f = TweetListFragment.newInstance(Constantes.TWEET_LIST_FAVS);
                        fab.show();
                        break;
                    case R.id.navigation_notifications:
                        f = new ProfileFragment();
                        fab.hide();
                        break;
                }

                if (f != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, f)
                            .commit();
                    return true;
                }
                return false;
            }
        });


        fab = findViewById(R.id.fab);
        // perfButton=findViewById(R.id.perfilButon);
        ivAvatar = findViewById(R.id.imagenperfil);


        getSupportActionBar().hide();

        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);
        Toast.makeText(this, "Token: " + token, Toast.LENGTH_LONG).show();

        getSupportFragmentManager()
                .beginTransaction()
                //YA NO SE PUEDE CARGAR UN FRAGMENTO ASI YA QUE SE MODIFICO..
                .add(R.id.fragmentContainer, new TweetListFragment())
                .add(R.id.fragmentContainer, TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL))
                .commit();
        botones();
        //Seteamos la imagen de perfil
        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL);
        //SE CARGA LA IMAGEN
        if (!photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(Constantes.API_MINITWITTER_FILES_URL + photoUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivAvatar);
        }
        profileViewModel.photoProfile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String photo) {
                Glide.with(DashboardActivity.this)
                        .load(Constantes.API_MINITWITTER_FILES_URL +photo)
                        //manejar erl chache de memeoria
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(ivAvatar);
            }
        });
    }

    private void botones() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //!! para llamar a un fragmento se hace de la sig manera.
                NuevoTweetDialogFragment dialog=new NuevoTweetDialogFragment();
                dialog.show(getSupportFragmentManager(),"NuevoTweetDialogFragment");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != RESULT_CANCELED) {
            if (requestCode == Constantes.SELECTFOTO_GALERYM) {
                if (data != null) {
                    //Ruta a la imagen
                    Uri imagenSeleccionada = data.getData();
                    String[] filePathColum = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imagenSeleccionada,
                            filePathColum, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int imagenindex = cursor.getColumnIndex(filePathColum[0]);//con esto nos dice el nombre del lugar en el que se encuentra la foto
                        String fotoPaht = cursor.getString(imagenindex);
                        profileViewModel.uploadPhooto(fotoPaht);
                        cursor.close();
                    }

                }
            }
        }
    }

    //cuando el permiso de selecion de datos externos
    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
//aceptar
        Intent seleccionarFoto=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(seleccionarFoto, Constantes.SELECTFOTO_GALERYM);//si la respuesta llega con uno ..es confirmada
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
//denegar
        Toast.makeText(this,"Sin permisos",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }

    public void cambiarvisibilidadDashboard(View view) {
        if(ivAvatar.getVisibility()==View.VISIBLE){
            ivAvatar.setVisibility(View.GONE);
        }else{
            ivAvatar.setVisibility(View.VISIBLE);
        }
    }
}
