package com.ejemplo.insert.database.mportafolio.Ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Constantes.SharedPreferencesManager;
import com.ejemplo.insert.database.mportafolio.R;
import com.ejemplo.insert.database.mportafolio.Ui.DashboardActivity;
import com.ejemplo.insert.database.mportafolio.pojomodel.RequestLogin;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseAuth;
import com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit.MiniTwitterClient;
import com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit.MiniTwitterService;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    Button btnLogin;
    TextView tvGoLgin;
    EditText etEmail,etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwiterServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        SignUpView();
        retrofitInit();

    }
    private void SignUpView() {

       etEmail = findViewById(R.id.emailmainactivity);
       etPassword = findViewById(R.id.passwordMainActivity);


        tvGoLgin=findViewById(R.id.notienesCuenta);
        btnLogin = findViewById(R.id.iniciarSecionMA);


        btnLogin.setOnClickListener(this);
        tvGoLgin.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.iniciarSecionMA:
                gotologin(v);
                break;
            case R.id.notienesCuenta:
                goToSignUp();
                break;
        }
    }

    private void gotologin(View view) {
        //ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(this);
        String email=etEmail.getText().toString();
        String password=etPassword.getText().toString();

        if(email.isEmpty()){
            etEmail.setError("Erorr en el emial");
        }else if(password.isEmpty()){
            etEmail.setError("Erorr en el password");
        }else{

            RequestLogin requestLogin=new RequestLogin(email,password);
            Call<ResponseAuth> call= miniTwiterServices.doLoging(requestLogin);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()) {

                       SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_TOKEN,response.body().getToken());
                        SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_USERNAME,response.body().getUsername());
                        SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_EMAIL,response.body().getEmail());
                        SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_PHOTOURL,response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_CREATED,response.body().getCreated());
                        //constante Booleana
                        SharedPreferencesManager.setSomeBooleanvalue(Constantes.PREF_ACTIVE,response.body().getActive());

                        Intent i =new Intent(MainActivity.this, DashboardActivity.class);
                      //  startActivity(i,options.toBundle());
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "ERROR!", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {

                }
            });
        }
    }

    private void goToSignUp() {
        Intent i = new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(i);
        finish();
    }
    private void retrofitInit() {
        miniTwitterClient= MiniTwitterClient.getInstance();
        miniTwiterServices=miniTwitterClient.getMiniTwitterService();
    }



}
