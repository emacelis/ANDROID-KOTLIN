package com.ejemplo.insert.database.juegoapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.ejemplo.insert.database.juegoapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class VerPerfil extends AppCompatActivity {
    private ListenerRegistration listenerRegistration = null;
    private TextView nombre,puntos,partidosjugados;
    private FirebaseFirestore db;
    private String uid;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        initFirebase();
        verPerfil();
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();
    }



    private void verPerfil() {

        nombre= findViewById(R.id.textViewNombreperfil);
        puntos= findViewById(R.id.textViewpuntosperfil);
        partidosjugados= findViewById(R.id.textpartuidosjugados);
        LottieAnimationView User = findViewById(R.id.animation_viewPerfil);

        User.playAnimation();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String Nombre=documentSnapshot.getString("name");

                    String punto=documentSnapshot.getLong("points").toString();
                    String partidasJugadas=documentSnapshot.getLong("partidasJugadas").toString();
                 nombre.setText("Nombre: "+Nombre);
                    puntos.setText("Puntos: "+punto);
                    partidosjugados.setText("Partidas jugadas: "+partidasJugadas);
                }
            }
        });
    }
}
