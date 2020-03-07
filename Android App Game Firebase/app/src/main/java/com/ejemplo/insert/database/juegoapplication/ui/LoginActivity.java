package com.ejemplo.insert.database.juegoapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.ejemplo.insert.database.juegoapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail,etPassword;
    private Button btnLogin,btregistro;
    //se declara el scrool view
    private ScrollView fromLogin;
    //se declara el rrogres bar
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private String email,password;
    boolean trylogin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        botones();
        changeLoginFormVisibiliti(true);
        eventos();
    }
    private void botones() {
        etEmail=findViewById(R.id.editTextemail);
        etPassword=findViewById(R.id.editTextPassword);
        btnLogin=findViewById(R.id.buttonLogin);
        fromLogin=findViewById(R.id.fromLogin);
        progressBar=findViewById(R.id.progressBarLogin);
        btregistro=findViewById(R.id.buttonRegistro);
    }
    private void eventos() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=etEmail.getText().toString();
                password=etPassword.getText().toString();
                if (email.isEmpty()){
                    etEmail.setError("Se necesita el Email");
                }else if(password.isEmpty()){
                    etPassword.setError("Se necesita el Password");
                }else {
                    //TODO:realizar Login con Firebase
                    changeLoginFormVisibiliti(false);
                    loginUser();
                }

            }
        });
        btregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser() {
        //metodo de fire base con click listener
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        trylogin=true;
                        if(task.isSuccessful()){
                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            //Log.w("TAG","signInError:",task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        //primero revisr paranmetros
        if (user!=null){
            //Almacenar informacion del usuario en Firebase


            //navegar a la sig actividad
            Intent intent=new Intent(LoginActivity.this,EncontrarJugadaActivity.class);
            startActivity(intent);
        }else{
            changeLoginFormVisibiliti(true);
            if(trylogin){
            etPassword.setError("Email o contraseña incorrectos");
            etPassword.requestFocus();
            }
        }
    }

    //Cambio de visivilidad
    private void changeLoginFormVisibiliti(boolean showForm) {
        progressBar.setVisibility(showForm ? View.GONE:View.VISIBLE);
        fromLogin.setVisibility(showForm ? View.VISIBLE:View.GONE);
    }
    /*
    //Metodo para verificar si ya inicio secion un usario no se veulva a identificar
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentuser=firebaseAuth.getCurrentUser();
        updateUI(currentuser);
    }
*/

}
