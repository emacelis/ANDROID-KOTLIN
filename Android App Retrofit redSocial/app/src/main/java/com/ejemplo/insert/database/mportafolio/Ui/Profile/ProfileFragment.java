package com.ejemplo.insert.database.mportafolio.Ui.Profile;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Data.ProfileViewModel;
import com.ejemplo.insert.database.mportafolio.R;
import com.ejemplo.insert.database.mportafolio.pojomodel.RequestUserProfile;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseUserProfile;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    ImageView ivAvatar;
    EditText etUsername,etemail,etPassword,etwebsite,etdesctipcion;
    Button btSave,btCambiarContrasena;
    PermissionListener allPersistableListenr;
    boolean loadingdata=true;



    public static ProfileFragment newInstance()
    {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //INSTANCIAR BIEN LAS INSTANCIAS...en klugar de this ..getActivity()
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        ivAvatar = v.findViewById(R.id.ivAvatarPerfil);
        etUsername = v.findViewById(R.id.editTextusername);
        etemail = v.findViewById(R.id.editTextEmail);
        etPassword = v.findViewById(R.id.editTextContraeña);
        etwebsite = v.findViewById(R.id.editTextwebsite);
        etdesctipcion = v.findViewById(R.id.editTextdescripcion);
        btSave = v.findViewById(R.id.buttonSave);
       // btCambiarContrasena = v.findViewById(R.id.cambiarcontrasena);

        // Eventos
        btSave.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String email = etemail.getText().toString();
            String descripcion = etdesctipcion.getText().toString();
            String website = etwebsite.getText().toString();
            String password = etPassword.getText().toString();

            if(username.isEmpty()) {
                etUsername.setError("El nombre de usuario es requerido");
            } else if(email.isEmpty()) {
                etemail.setError("El email es requerido");
            } else if(password.isEmpty()) {
                etPassword.setError("La contraseña es requerida");
            } else {
                RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, descripcion, website, password);
                profileViewModel.updateProfile(requestUserProfile);
                Toast.makeText(getActivity(), "Enviando información al servidor", Toast.LENGTH_SHORT).show();
                btSave.setEnabled(false);
            }
        });

      //  btCambiarContrasena.setOnClickListener(view -> {
      //     Toast.makeText(getActivity(), "Click on save", Toast.LENGTH_SHORT).show();
      //  });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //invocar permisos
                checkPermision();

            }
        });


        // ViewModel
        profileViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(@Nullable ResponseUserProfile responseUserProfile) {
                loadingdata = false;
                etUsername.setText(responseUserProfile.getUsername());
                etemail.setText(responseUserProfile.getEmail());
                etwebsite.setText(responseUserProfile.getWebsite());
                etdesctipcion.setText(responseUserProfile.getDescripcion());

                if(!responseUserProfile.getPhotoUrl().isEmpty()) {
                    Glide.with(getActivity())
                            .load(Constantes.API_MINITWITTER_FILES_URL + responseUserProfile.getPhotoUrl())
                            //manejar erl chache de memeoria
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }

                if(!loadingdata) {
                    btSave.setEnabled(true);
                    Toast.makeText(getActivity(), "Datos cargados correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profileViewModel.photoProfile.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String photo) {

                    Glide.with(getActivity())
                            .load(Constantes.API_MINITWITTER_FILES_URL + photo)
                            //manejar erl chache de memeoria
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivAvatar);

            }
        });
        return v;

    }

    private void checkPermision() {
        //checar permios
        PermissionListener dialogOnDeniedPermisionListener=
                DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                .withTitle("Permisos")
                .withMessage("Se requeren permisos para seleccionar foto")
                .withButtonText("Aceptar")
                .withIcon(R.mipmap.ic_launcher)
                .build();
        allPersistableListenr=new CompositePermissionListener(
                //se caseta a (permison listener )en el dashobioard implements Listener..
                (PermissionListener)getActivity(),
                dialogOnDeniedPermisionListener
        );
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPersistableListenr)
                .check();
    }

}
