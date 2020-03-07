package com.ejemplo.insert.database.juegoapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ejemplo.insert.database.juegoapplication.R;
import com.ejemplo.insert.database.juegoapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity {
    EditText etEmail,etPassword,name;
    private Button btRegistro;
    //se declara el scrool view
    private ScrollView fromRegistro;
    //se declara el rrogres bar
    private ProgressBar progressBar;
    //IMPORTANTE SIEMPRE INICIALISAR UNA VARIABLE A FIREBASE PARA TRAER LOS METODOS
    private FirebaseAuth firebaseAuth;
    //Se necesita dejar registro del usuario ..para hacer rating
    //para eso se usa firebase-firestore
    private FirebaseFirestore dbFirebase;

    private String Nombre,Email,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        name=findViewById(R.id.editTextNombrerREGISTRO);
        btRegistro=findViewById(R.id.buttonLogRegistro);
        etEmail=findViewById(R.id.editTextemailREGISTRO);
        etPassword=findViewById(R.id.editTextPasswordREGISTRO);
        fromRegistro=findViewById(R.id.fromRegistroREGISTRO);
        progressBar=findViewById(R.id.progressBarRegistroREGISTRO);

        //IMPORTANTE INSTANCIAR FIREBASE
        firebaseAuth=FirebaseAuth.getInstance();
        dbFirebase=FirebaseFirestore.getInstance();
        changeRegistroFormVisibiliti(true);
        eventos();

    }

    private void eventos() {
        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nombre=name.getText().toString();
                Email=etEmail.getText().toString();
                Password=etPassword.getText().toString();
                changeRegistroFormVisibiliti(false);
                if (Nombre.isEmpty()){
                    name.setError("Nombre necesario");
                }else if (Email.isEmpty()){
                    etEmail.setError("Email necesario");
                }else if(Password.isEmpty()){
                    etPassword.setError("Password necesario");
                }else {
                    //TODO:realizar registro Firebase
                    crearUsuario();
                }

            }
        });
    }

    private void crearUsuario() {
        changeRegistroFormVisibiliti(false);

        //firebase solo acepta dos campos en auth ...email y password por defecto
        //como es una operacion asincrona se cuida el manejo de informacion con
        firebaseAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    //recuerda que con Oncompletelistenr ya tiene el metodo de respuesta integrado
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                       FirebaseUser user=firebaseAuth.getCurrentUser();//con esta linea se obtiene el usuario
                       UpdateUi(user);//metdod de interfas de usuario
                    }else{
                        Toast.makeText(RegistroActivity.this,"Error al registrar usuario",Toast.LENGTH_SHORT).show();
                    }
                    }
                });
    }

    private void UpdateUi(FirebaseUser user) {
        //primero revisr paranmetros
        if (user!=null){
            //creamos un Usuario para guardar los datos
            User nuevoUsuario=new User(Nombre,0,0);
            //Almacenar informacion del usuario en Firebase
            dbFirebase.collection("users")
                    .document(user.getUid())
                    .set(nuevoUsuario)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            finish();
                            //navegar a la sig actividad
                            Intent intent=new Intent(RegistroActivity.this,EncontrarJugadaActivity.class);
                            startActivity(intent);
                        }
                    });



        }else{
            changeRegistroFormVisibiliti(true);
            etPassword.setError("Email, nombre o contraseña Incorrecto");
            etPassword.requestFocus();
        }
    }

    //Cambio de visivilidad
    private void changeRegistroFormVisibiliti(boolean showForm) {
        progressBar.setVisibility(showForm ? View.GONE:View.VISIBLE);
        fromRegistro.setVisibility(showForm ? View.VISIBLE:View.GONE);
    }

}
