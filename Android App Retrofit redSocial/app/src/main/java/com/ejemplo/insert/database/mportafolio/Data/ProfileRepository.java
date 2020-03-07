package com.ejemplo.insert.database.mportafolio.Data;


import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.widget.Toast;

import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Constantes.MyApp;
import com.ejemplo.insert.database.mportafolio.Constantes.SharedPreferencesManager;
import com.ejemplo.insert.database.mportafolio.pojomodel.RequestUserProfile;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseUploadPhoto;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseUserProfile;
import com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit.AuthTweetService;
import com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit.AuthTwitterClient;

import java.io.File;

import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    AuthTweetService authTwiterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<ResponseUserProfile> userProfile;//se cambia la variable debido a que se le añade un nuevo tweet
    MutableLiveData<String> photoProfile;//colocarlo en el constructor

     ProfileRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwiterService = authTwitterClient.getAuthTwitterService();
        userProfile=getProfile();//si se modifica la laista de tweets le avisara a
         if(photoProfile==null){
         photoProfile=new MutableLiveData<>();
         }
    }

    //GetAllTweets
    public MutableLiveData<ResponseUserProfile> getProfile() {
        //para poder tener todos los tweets
        //final MutableLiveData<List<Tweet>> data = new MutableLiveData<>();
        //para todoss los tweets
        if (userProfile == null) {
            userProfile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call=authTwiterService.getProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(),"Error al cargar datos",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(),"Error en la red",Toast.LENGTH_LONG).show();
            }
        });
        return userProfile;
    }
    //UNA VES TERMINADO SE TIENE QUE MANDAR ESTO AL VIEW MODEL "PRIOFILEVIWEMODEL"

    public void updateProfile(RequestUserProfile requestUserProfile) {
        Call<ResponseUserProfile> call = authTwiterService.updateProfile(requestUserProfile);

        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()) {
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public MutableLiveData<String>getPhotoProfile(){
         return photoProfile;
    }

public void uploadPhooto(String pohotoPath){
    File file= new File(pohotoPath);
    RequestBody requestBody=RequestBody.create(MediaType.parse("image/jpg"),file);
    //se carga el servicio
    Call<ResponseUploadPhoto> call=authTwiterService.uploadProfilePhooto(requestBody);
    call.enqueue(new Callback<ResponseUploadPhoto>() {
        @Override
        public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
            if(response.isSuccessful()) {
                SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_PHOTOURL,response.body().getFilename());
                photoProfile.setValue(response.body().getFilename());
            } else {
                Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
            Toast.makeText(MyApp.getContext(), "Error en la Red", Toast.LENGTH_SHORT).show();

        }
    });
}



}
