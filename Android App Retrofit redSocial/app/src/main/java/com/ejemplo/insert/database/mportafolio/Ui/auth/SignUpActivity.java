package com.ejemplo.insert.database.mportafolio.Ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ejemplo.insert.database.mportafolio.Constantes.Constantes;
import com.ejemplo.insert.database.mportafolio.Constantes.SharedPreferencesManager;
import com.ejemplo.insert.database.mportafolio.R;
import com.ejemplo.insert.database.mportafolio.Ui.DashboardActivity;
import com.ejemplo.insert.database.mportafolio.pojomodel.RequestSignUp;
import com.ejemplo.insert.database.mportafolio.pojomodel.ResponseAuth;
import com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit.MiniTwitterClient;
import com.ejemplo.insert.database.mportafolio.pojomodel.Retrofit.MiniTwitterService;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView tvGoLgin;
    EditText etUserName, EtEmail, EtPasword;

    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwiterServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        retrofitInit();
        SignUpView();
    }

    private void SignUpView() {
        etUserName = findViewById(R.id.usernameup);
        EtEmail = findViewById(R.id.emailup);
        EtPasword = findViewById(R.id.passwordup);

        btnLogin = findViewById(R.id.iniciarSecionlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUp();
            }
        });

        //TEXT VIEW
        tvGoLgin = findViewById(R.id.textviewgologin);
        tvGoLgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwiterServices = miniTwitterClient.getMiniTwitterService();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iniciarSecionlogin:

                break;
            case R.id.textviewgologin:
                goToSignUp();
                break;
        }
    }

    private void goToSignUp() {
        String username = etUserName.getText().toString();
        String email = EtEmail.getText().toString();
        String password = EtPasword.getText().toString();

        if (username.isEmpty()) {
            etUserName.setError("El nombre de usuario es requerido");
        } else if (email.isEmpty()) {
            EtEmail.setError("El email es requerido");
        } else if (password.isEmpty() || password.length() < 4) {
            EtPasword.setError("La contraseña es muy corta");
        } else {
            //Codigo que te da bacend
            String code = "UDEMYANDROID";
            RequestSignUp requestSignUp = new RequestSignUp(username, email, password, code);
            Call<ResponseAuth> call = miniTwiterServices.doSignup(requestSignUp);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()) {

                        SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringvalue(Constantes.PREF_CREATED, response.body().getCreated());//constante Booleana
                        SharedPreferencesManager.setSomeBooleanvalue(Constantes.PREF_ACTIVE, response.body().getActive());

                        Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(i);
                        //destruir este Activity por ui
                        finish();
                    } else if (response.code() == 400) {
                        Log.d("Tag1", "Error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Log.d("Tag1", "Error con la Red");
                }
            });
        }
    }

}