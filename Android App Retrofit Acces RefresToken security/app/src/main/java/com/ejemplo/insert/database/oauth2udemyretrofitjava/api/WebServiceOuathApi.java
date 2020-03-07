package com.ejemplo.insert.database.oauth2udemyretrofitjava.api;

import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.MovimientoBancario;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.Token;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WebServiceOuathApi {
    @GET("/api/user")
    Call<List<User>>obtenerUsuarios();

    //Call<Void> fijese que como no nos devuelve nada solo se one <Void>
    @POST("/api_create_user")
    Call<Void> crearUsuario(@Body User user);

    //OAHU2
    //obtenertoken con paswword
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<Token>obtenerToken(
            @Header("Authorization")String authotization,
            @Field("username")String username,
            @Field("password")String password,
            @Field("grant_type")String grantType
    );

    //OAHU2
    //obtener token con refreshToken
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<Token>obtenerTokenconRefreshToken(
            @Header("Authorization")String authotization,
            @Field("refresh_token")String refreshToken,
            @Field("grant_type")String grantType
    );

    @GET("/api/oauth2/movimiento_bancario")
    Call<List<MovimientoBancario>>obtenermovimeitnos(@Header("Authorization")String accesToken);

    @POST("/api/oauth2/movimiento_bancario_user")
    Call<List<MovimientoBancario>>obtenermovimeitnosUser(@Body User user);

}
