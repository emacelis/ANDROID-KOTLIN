package com.ejemplo.insert.database.juegoapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ejemplo.insert.database.juegoapplication.R;
import com.ejemplo.insert.database.juegoapplication.app.Constantes;
import com.ejemplo.insert.database.juegoapplication.app.MyApp;
import com.ejemplo.insert.database.juegoapplication.model.Jugada;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;
/*https://assets9.lottiefiles.com/packages/lf20_1Wy2m3.json*/
public class EncontrarJugadaActivity extends AppCompatActivity {
    private TextView textViewMessage;
    private ProgressBar progressBar;

    //Cuando sale un activiti de juegfo, o cuando sale un activity de Loading
    private ScrollView layoutProgresBar,layoutMenuJuego;

    private Button buttonJugar,buttonRanking;

    //PARA INICIALIZAR FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    //identificador de usuario
    private String uid,jugadaId;

    //se coloca a null por si el metodo no encuentra partida
    private ListenerRegistration listenerRegistration=null;

    //PARA EL MANEJO DE ANIMACIONES SE COLOCA UN ID AL LOOTIE VIEW DEL XML
    //Y SE DECLARA UNA VARIABLE PARA TRAER SUS METODOS
    private LottieAnimationView animationView;
    //RECUERDA INISIALISARLA






    //CLASE QUE ENCUENTRA JUGADAS, ENROLA ALOS JUGADORES, O CREA UNA NUEVA EN CASO DE NO EXISTIR
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encontrar_jugada);

        layoutProgresBar=findViewById(R.id.layoutProgresBar);
        layoutMenuJuego=findViewById(R.id.menujuego);
        buttonJugar=findViewById(R.id.buttonJugar);
        buttonRanking=findViewById(R.id.buttonRatink);


        iniciarjuego();
        //IMPORTANTE
        //inicializar firebase
        iniciarFirebase();
        eventos();


    }

    private void iniciarFirebase() {
        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        uid=firebaseUser.getUid();
        //PARA LA CARGA DE ANIMACIONES SE TRAE LA LIBRERIA LOLITTE
        //SE TRABAJA CON ANDROIDX
        //Y SE CREA UNA NUEVA CARPETA Foilder->Assets Foilder
        //que guardara las animaciones
    }

    private void eventos() {
        buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changemenuVisibility(false);
                //
                buscarJugadaLibre();
            }
        });
        buttonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    Intent intent=new Intent(EncontrarJugadaActivity.this,VerPerfil.class);
    startActivity(intent);

            }
        });
    }

    private void buscarJugadaLibre() {
        textViewMessage.setText("Buscando una partida libre");
        //EVITAR QUE UN USARIO JUEGE CONTRA SI MISMO
        //n firesbase no existe el condicional no igual
        animationView.playAnimation();

        db.collection("jugadas")
                .whereEqualTo("jugadorDosId","")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size()==0){
                            //CREAR JUGADA CUANDO LAS JUGADAS SEAN CERO
                            crearnuevajugada();

                        }else{
                            //Evitar jugador vs el mismo
                            boolean encontrado=false;

                            for(DocumentSnapshot docJugada:task.getResult().getDocuments()) {
                                if (!docJugada.get("jugadorUnoId").equals(uid)) {
                                    encontrado=true;

                                    jugadaId = docJugada.getId();
                                    //creamos un objeto jugada para ingresarlo
                                    Jugada jugada = docJugada.toObject(Jugada.class);
                                    //seteamos nuestro jugador en la jugada
                                    jugada.setJugadorDosId(uid);

                                    //actualizar jugada para que se alamcene en el db  y no aparesca estsa
                                    //jugada para otro jugador visible
                                    db.collection("jugadas")
                                            .document(jugadaId)
                                            .set(jugada)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    //animacion de inicio..se leinidca 0 repeticiones ..solo una
                                                    textViewMessage.setText("Partida libre encontrada,Comienza el juegp");
                                                    animationView.setRepeatCount(0);
                                                    animationView.setAnimation("checked_animation.json");
                                                    animationView.playAnimation();
                                                    //el habndelr para la ejecucion del codigo por unos seg para preparar al jugador
                                                    final Handler handler = new Handler();
                                                    final Runnable runnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //ejecutar el juego despues de un tiempo determinado
                                                            //INICIA EL JUEGO
                                                            startGame();
                                                        }
                                                    };
                                                    handler.postDelayed(runnable, 1500);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            changemenuVisibility(true);
                                            Toast.makeText(EncontrarJugadaActivity.this,
                                                    "Error al entrar a la partida", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                }
                                //SI DESPUES DE HABER ITERADO POR TODAS LAS JUGASAS LIBRES..NO HEMOS ENCONTRADO NINGUNA
                                if (!encontrado) {
                                    crearnuevajugada();
                                }
                            }
                        }

                    }
                });
    }

    private void crearnuevajugada() {
        textViewMessage.setText("Creando jugada nueva...");
        Jugada nuevajugada = new Jugada(uid);

        db.collection("jugadas")
                .add(nuevajugada)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //si esta bien, SE obtiene el id y se guarda
                        //y eso se hace mediante el documento que nos regresa documentReference
                        jugadaId=documentReference.getId();
                        //Tenemos creada la jugada devemsos esperar a otro jufador
                        esperarOtroJugador();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                changemenuVisibility(true);
                Toast.makeText(EncontrarJugadaActivity.this,
                        "Error al crear jugada nueva",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void esperarOtroJugador() {
        textViewMessage.setText("Esperando a otro jugador...");

        //Con adSnapshotListener va a estar atento a algun cambvio en el docuemnto seleccionado
        listenerRegistration=db.collection("jugadas")
                .document(jugadaId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (!documentSnapshot.get("jugadorDosId").equals("")){

                            textViewMessage.setText("!Tienes un retador¡ Comienza la partida");
                            //animacion de inicio..se leinidca 0 repeticiones ..solo una
                            animationView.setRepeatCount(0);
                            animationView.setAnimation("checked_animation.json");
                            animationView.playAnimation();


                            //el habndelr para la ejecucion del codigo por unos seg para preparar al jugador
                            final Handler handler=new Handler();
                            final Runnable runnable=new Runnable() {
                                @Override
                                public void run() {
                                    //ejecutar el juego despues de un tiempo determinado
                                    startGame();
                                }
                            };
                            //se ejecuta el handler llamnado al metodo y el tiempo de delay
                            handler.postDelayed(runnable,2000);

                        }
                    }
                });
    }

    private void startGame() {
        //para que no este continuamente escuchando el listebner de arriba
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }

        Intent intent=new Intent(EncontrarJugadaActivity.this,GameActivity.class);
        intent.putExtra(Constantes.EXTRA_JUGADA_ID,jugadaId);
        startActivity(intent);
    }

    private void iniciarjuego() {
        animationView=findViewById(R.id.animation_view);
        textViewMessage=findViewById(R.id.textViewLogging);
        progressBar=findViewById(R.id.progressBarEncontrarJugada);
        progressBar.setIndeterminate(true);
        textViewMessage.setText("Cargando...");
        changemenuVisibility(true);

        //Se inicializa el juego a "" si es que el usario abandona la partuida a la mitad del juego
        jugadaId="";
    }

    private void changemenuVisibility(boolean showMenu) {
        layoutProgresBar.setVisibility(showMenu ? View.GONE : View.VISIBLE);
        layoutMenuJuego.setVisibility(showMenu ? View.VISIBLE : View.GONE);
    }
    //por pauseo en el metodo de carga
    @Override
    protected void onResume(){
        super.onResume();
        if(jugadaId!=""){
            changemenuVisibility(false);
            esperarOtroJugador();
        }else{
            changemenuVisibility(true);
        }
    }


    @Override
    protected void onStop() {
        if(listenerRegistration != null) {
            listenerRegistration.remove();
        }

        if(jugadaId != "") {
            db.collection("jugadas")
                    .document(jugadaId)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            jugadaId = "";
                        }
                    });

        }

        super.onStop();
    }
}

/*
*
*     private TextView textViewMessage;
    private ProgressBar progressBar;

    //Cuando sale un activiti de juegfo, o cuando sale un activity de Loading
    private ScrollView layoutProgresBar,layoutMenuJuego;

    private Button buttonJugar,buttonRanking;

    //PARA INICIALIZAR FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    //identificador de usuario
    private String uid,jugadaId;

    //se coloca a null por si el metodo no encuentra partida
    private ListenerRegistration listenerRegistration=null;

    //PARA EL MANEJO DE ANIMACIONES SE COLOCA UN ID AL LOOTIE VIEW DEL XML
    //Y SE DECLARA UNA VARIABLE PARA TRAER SUS METODOS
    private LottieAnimationView animationView;
    //RECUERDA INISIALISARLA






//CLASE QUE ENCUENTRA JUGADAS, ENROLA ALOS JUGADORES, O CREA UNA NUEVA EN CASO DE NO EXISTIR
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encontrar_jugada);

        layoutProgresBar=findViewById(R.id.layoutProgresBar);
        layoutMenuJuego=findViewById(R.id.menujuego);
        buttonJugar=findViewById(R.id.buttonJugar);
        buttonRanking=findViewById(R.id.buttonRatink);


        iniciarjuego();
        //IMPORTANTE
        //inicializar firebase
        iniciarFirebase();
        eventos();


    }

    private void iniciarFirebase() {
    firebaseAuth=FirebaseAuth.getInstance();
    db=FirebaseFirestore.getInstance();
    firebaseUser=firebaseAuth.getCurrentUser();
    uid=firebaseUser.getUid();
    //PARA LA CARGA DE ANIMACIONES SE TRAE LA LIBRERIA LOLITTE
        //SE TRABAJA CON ANDROIDX
        //Y SE CREA UNA NUEVA CARPETA Foilder->Assets Foilder
        //que guardara las animaciones
    }

    private void eventos() {
        buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changemenuVisibility(false);
                //
                buscarJugadaLibre();
            }
        });
        buttonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void buscarJugadaLibre() {
        textViewMessage.setText("Buscando una partida libre");
        //EVITAR QUE UN USARIO JUEGE CONTRA SI MISMO
        //n firesbase no existe el condicional no igual
        animationView.playAnimation();

        db.collection("jugadas")
                .whereEqualTo("jugadorDosId","")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size()==0){
                            //CREAR JUGADA CUANDO LAS JUGADAS SEAN CERO
                            crearnuevajugada();

                        }else{
                            //Evitar jugador vs el mismo
                            boolean encontrado=false;

                                    for(DocumentSnapshot docJugada:task.getResult().getDocuments()) {
                                        if (!docJugada.get("jugadorUnoId").equals(uid)) {
                                            encontrado=true;

                                            jugadaId = docJugada.getId();
                                            //creamos un objeto jugada para ingresarlo
                                            Jugada jugada = docJugada.toObject(Jugada.class);
                                            //seteamos nuestro jugador en la jugada
                                            jugada.setJugadorDosId(uid);

                                            //actualizar jugada para que se alamcene en el db  y no aparesca estsa
                                            //jugada para otro jugador visible
                                            db.collection("jugadas")
                                                    .document(jugadaId)
                                                    .set(jugada)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            //animacion de inicio..se leinidca 0 repeticiones ..solo una
                                                            textViewMessage.setText("Partida libre encontrada,Comienza el juegp");
                                                            animationView.setRepeatCount(0);
                                                            animationView.setAnimation("checked_animation.json");
                                                            animationView.playAnimation();
                                                            //el habndelr para la ejecucion del codigo por unos seg para preparar al jugador
                                                            final Handler handler = new Handler();
                                                            final Runnable runnable = new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    //ejecutar el juego despues de un tiempo determinado
                                                                    //INICIA EL JUEGO
                                                                    startGame();
                                                                }
                                                            };
                                                            handler.postDelayed(runnable, 1500);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    changemenuVisibility(true);
                                                    Toast.makeText(EncontrarJugadaActivity.this,
                                                            "Error al entrar a la partida", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            break;
                                        }
                                        //SI DESPUES DE HABER ITERADO POR TODAS LAS JUGASAS LIBRES..NO HEMOS ENCONTRADO NINGUNA
                                        if (!encontrado) {
                                        crearnuevajugada();
                                        }
                                        }
                                    }

                        }
                });
    }

    private void crearnuevajugada() {
        textViewMessage.setText("Creando jugada nueva...");
        Jugada nuevajugada = new Jugada(uid);

        db.collection("jugadas")
                .add(nuevajugada)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //si esta bien, SE obtiene el id y se guarda
                        //y eso se hace mediante el documento que nos regresa documentReference
                        jugadaId=documentReference.getId();
                        //Tenemos creada la jugada devemsos esperar a otro jufador
                        esperarOtroJugador();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                changemenuVisibility(true);
                Toast.makeText(EncontrarJugadaActivity.this,
                        "Error al crear jugada nueva",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void esperarOtroJugador() {
        textViewMessage.setText("Esperando a otro jugador...");

        //Con adSnapshotListener va a estar atento a algun cambvio en el docuemnto seleccionado
        listenerRegistration=db.collection("jugadas")
                .document(jugadaId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshot.get("jugadorDosId").equals("")){

                    textViewMessage.setText("!Tienes un retador¡ Comienza la partida");
                    //animacion de inicio..se leinidca 0 repeticiones ..solo una
                    animationView.setRepeatCount(0);
                    animationView.setAnimation("checked_animation.json");
                    animationView.playAnimation();


                    //el habndelr para la ejecucion del codigo por unos seg para preparar al jugador
                    final Handler handler=new Handler();
                    final Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            //ejecutar el juego despues de un tiempo determinado
                            startGame();
                        }
                    };
                    //se ejecuta el handler llamnado al metodo y el tiempo de delay
                    handler.postDelayed(runnable,2000);

                }
                    }
                });
    }

    private void startGame() {
        //para que no este continuamente escuchando el listebner de arriba
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }

        Intent intent=new Intent(EncontrarJugadaActivity.this,GameActivity.class);
        intent.putExtra(Constantes.EXTRA_JUGADA_ID,jugadaId);
        startActivity(intent);
    }

    private void iniciarjuego() {
        animationView=findViewById(R.id.animation_view);
        textViewMessage=findViewById(R.id.textViewLogging);
        progressBar=findViewById(R.id.progressBarEncontrarJugada);
        progressBar.setIndeterminate(true);
        textViewMessage.setText("Cargando...");
        changemenuVisibility(true);

        //Se inicializa el juego a "" si es que el usario abandona la partuida a la mitad del juego
        jugadaId="";
    }

    private void changemenuVisibility(boolean showMenu) {
        layoutProgresBar.setVisibility(showMenu ? View.GONE : View.VISIBLE);
        layoutMenuJuego.setVisibility(showMenu ? View.VISIBLE : View.GONE);
    }
    //por pauseo en el metodo de carga
    @Override
    protected void onResume(){
        super.onResume();
        if(jugadaId!=""){
            changemenuVisibility(false);
            esperarOtroJugador();
        }else{
            changemenuVisibility(true);
        }
    }


    @Override
    protected void onStop() {
        db.collection("jugadas")
                .document(jugadaId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        jugadaId="";
                    }
                });

        super.onStop();
    }
* */
//https://assets6.lottiefiles.com/datafiles/G3hZTKzfC5cDiBV/data.json
//https://assets8.lottiefiles.com/datafiles/qv6UAmxdn2gUuuQ/data.json