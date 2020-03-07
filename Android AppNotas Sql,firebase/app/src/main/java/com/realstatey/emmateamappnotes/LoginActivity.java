package com.realstatey.emmateamappnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login,register;
    FirebaseAuth auth;//ES INPORTANTE DECLARAR LA VAIABLE FIREBASE AUTH


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();//ES INPORTANTE DECLARAR LA INSTANCIA


        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.registro);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userE = email.getText().toString();
                String passE = password.getText().toString();

                if (TextUtils.isEmpty(userE)) {
                    Toast.makeText(getApplicationContext(), "Coloca Correo", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(passE)) {
                    Toast.makeText(getApplicationContext(), "Coloca Password", Toast.LENGTH_SHORT).show();
                }

                auth.signInWithEmailAndPassword(userE, passE)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Correo o password incoprrecto",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();//importante cerrar
                        }
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,Registro.class);
                startActivity(intent);
            }
        });

    }
}
