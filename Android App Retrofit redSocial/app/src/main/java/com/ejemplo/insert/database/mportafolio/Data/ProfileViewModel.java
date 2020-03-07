package com.ejemplo.insert.database.mportafolio.Data;

import android.app.Application;

import com.ejemplo.insert.database.mportafolio.pojomodel.RequestUserProfile;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseUserProfile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {
    public ProfileRepository profileRepository;
    public  LiveData<ResponseUserProfile> userProfile;
    public LiveData<String> photoProfile;

    public ProfileViewModel(@NonNull Application application){
        super(application);
        profileRepository=new ProfileRepository();
        userProfile=profileRepository.getProfile();
        ///instanciar todos os metodos que se hacen
        photoProfile=profileRepository.getPhotoProfile();
    }
//SE INVOCA EL METODO DE ProfileRepositori updateProfile


    public  void updateProfile(RequestUserProfile requestUserProfile){
        profileRepository.updateProfile(requestUserProfile);
    }

    //FOTO
    public  void uploadPhooto(String photo){
        profileRepository.uploadPhooto(photo);
    }
}
