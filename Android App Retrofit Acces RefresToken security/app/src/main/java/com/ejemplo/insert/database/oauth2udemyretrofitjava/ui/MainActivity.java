package com.ejemplo.insert.database.oauth2udemyretrofitjava.ui;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ejemplo.insert.database.oauth2udemyretrofitjava.R;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.api.WebServiceOuath2;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.api.WebServiceOuathApi;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.MovimientoBancario;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.Token;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.User;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.shared_preferences.TokenManager;

import org.w3c.dom.ls.LSInput;

import java.util.List;

import static com.ejemplo.insert.database.oauth2udemyretrofitjava.shared_preferences.TokenManager.SHARED_PREFERENCES;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btObtenerToken;
    private Button btCrearUsuario;
    private Button btVerTodoslosUsuairos;
    private Button btTodosLosMovimientosBancarios;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();
    }

    private void setUpView() {
        //MODE_PRIVATE SOLO NUESTRA APLICACION PUEDE ACCEDER A ELLOS
        tokenManager=TokenManager.getInstance(getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE));
        etUsername=findViewById(R.id.etUserName);
        etPassword=findViewById(R.id.etPassw);
        btObtenerToken=findViewById(R.id.btObtenerToken);
        btObtenerToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               obtenerTokenMetodo();
            }
        });

        btCrearUsuario=findViewById(R.id.btCrearUsuario);
        btCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario();
            }
        });


        btVerTodoslosUsuairos=findViewById(R.id.btVerTodosUsuarios);
        btVerTodoslosUsuairos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verTodoslosUsuarios();
            }
        });


        btTodosLosMovimientosBancarios=findViewById(R.id.btVerTodosLosMovBancarios);
        btTodosLosMovimientosBancarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verMovimientosBancarios();
            }
        });
    }

    private void verMovimientosBancarios() {
        Call<List<MovimientoBancario>>call=WebServiceOuath2
                .getInstance()
                .createService(WebServiceOuathApi.class)
                .obtenermovimeitnos("Bearer"+tokenManager.getToken().getAccesToken());
        call.enqueue(new Callback<List<MovimientoBancario>>() {
            @Override
            public void onResponse(Call<List<MovimientoBancario>> call, Response<List<MovimientoBancario>> response) {
                if(response.code()==200){
                    for(int i=0;i<response.body().size();i++){
                        Log.d("TAG1","UserId: "+response.body().get(i).getUserId()
                        +"Importe: "+response.body().get(i).getImporte()+
                                "Nombre"+response.body().get(i).getName());
                    }
                    }else if(response.code()==404){
                    Log.d("TAG1","Eror");
                }else{
                    Log.d("TAG1","Eror");
                }
            }

            @Override
            public void onFailure(Call<List<MovimientoBancario>> call, Throwable t) {

            }
        });
    }

    private void obtenerTokenMetodo() {
        //las claves nos la da el backend,se pasa a BYTES para que se pase a base64
        String authHeader="Basic "+Base64.encodeToString(("androidApp:123").getBytes(),Base64.NO_WRAP);

        Call<Token>call= WebServiceOuath2
                .getInstance()
                .createService(WebServiceOuathApi.class)
                .obtenerToken(
                       // Base64_androidApp:123
                        authHeader,
                        etUsername.getText().toString(),
                        etPassword.getText().toString(),
                        "password"
                );
        call.enqueue(new Callback<Token>() {
            //Se crea el objeto Token
            Token token=new Token();

            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.code()==200){
                    Log.d("TAG1","Access Token:"+response.body().getAccesToken()
                    +"REFRESH TOKEN:"+response.body().getRefreshToken());

                    //se asigna valor al token
                    token=response.body();
                    //GUARDAMOS EL TOKEN
                    tokenManager.savedToken(token);
                    //TODO start ew Activity
                    startActivity(new Intent(getApplicationContext(),LogeadoActivity.class));
                }else{
                    Log.d("TAG1","Error");
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
    }

    private void crearUsuario() {
        User user=new User();
        user.setPassword(etPassword.getText().toString());
        user.setUsername(etUsername.getText().toString());

        Call<Void>call=WebServiceOuath2
                .getInstance()
                .createService(WebServiceOuathApi.class)
                .crearUsuario(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==201){
                    Log.d("TAG1","Creando Usuario Ciorrectamete");
                }else{
                    Log.d("TAG1","Error");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void verTodoslosUsuarios() {
        Call<List<User>> call = WebServiceOuath2
                .getInstance()
                .createService(WebServiceOuathApi.class)
                .obtenerUsuarios();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.code()==200){
                    for(int i=0;i<response.body().size();i++){
                        Log.d("TAG1","Username:"+response.body().get(i).getUsername());
                    }
                }else {
                    Log.d("TAG1","Error");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}
