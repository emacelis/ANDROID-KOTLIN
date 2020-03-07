package com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit;

import com.ejemplo.insert.database.mportafolio.pojomodel.RequestCreateTwitter;
import com.ejemplo.insert.database.mportafolio.pojomodel.RequestUserProfile;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseUploadPhoto;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseUserProfile;
import com.ejemplo.insert.database.mportafolio.pojomodel.Tweet;
import com.ejemplo.insert.database.mportafolio.pojomodel.TweetDeleted;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthTweetService {

    @GET("tweets/all")
    Call<List<Tweet>> getallTweets();


    //para twittear..con el @Body cuerpo de la petición
    @POST("tweets/create")
    Call<Tweet> createTweet(@Body RequestCreateTwitter requestCreateTwitter);

    //LIKE/DISLLIKE CORAZON
    //SE LE AÑADE EL DECORADOR @PATH Para indicarle que sel va a pasar un parametro..en este
    //caso el id que se le va a modificar
    @POST("tweets/like/{idTweet}")
    Call<Tweet>likeTweet(@Path("idTweet")int idTweet);
    //ahora en el respoitorio se añade la petición

    @DELETE("tweets/{idTweet}")
    Call<TweetDeleted>deleteTweet(@Path("idTweet")int idTweet);

    //CARGAR DATOS NUEVOS DE PERFIL/
    //OBTENERINFOMACION
    @GET("users/profile")
    Call<ResponseUserProfile> getProfile();

    @PUT("users/profile")
    Call<ResponseUserProfile> updateProfile(@Body RequestUserProfile requestUserProfile);

    //FOTOGRAFIA
    //como es una peticvion se hace con POST
    @Multipart //se enviara un fichero por partes
    @POST("users/uploadprofilephoto")
    Call<ResponseUploadPhoto>uploadProfilePhooto(@Part("file\"; filename=\"photo.jpeg\" ")RequestBody file);
}
