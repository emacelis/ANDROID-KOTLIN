package com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit;


import com.ejemplo.insert.database.mportafolio.pojomodel.RequestLogin;
import com.ejemplo.insert.database.mportafolio.pojomodel.RequestSignUp;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {
    @POST("auth/login")
    Call<ResponseAuth> doLoging(@Body RequestLogin requestLogin);

    @POST("auth/signup")
    Call<ResponseAuth>doSignup(@Body RequestSignUp requestSignUp);


}
