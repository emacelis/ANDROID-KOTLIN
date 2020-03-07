package com.realstatey.emmateamappnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;//DEFIIR EL RECYCLER VIEW
    List<Notes>notesList;//definir la lista para notas
    FloatingActionButton fab;
    //NotasAdapter adapter;//definir el NOTASADAPTER para notas
    ImageView trash;
    //adaptador de firebase  dato,clase ViewHolder

    FirebaseRecyclerAdapter<Notes,NotasAdapter.ViewHolder> adapter;
    DatabaseReference databaseReference;//DEFINIR LO QUE VAS A TRABAJAR
    //en firebaseUI te vana a dar en un recycler vuiew un adaptador

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //createDatos();
        //IMPORTANTE CREAR REFERENCIAS  dame la instancia de la base de datoos
        databaseReference= FirebaseDatabase.getInstance().getReference();
        fab=(FloatingActionButton)findViewById(R.id.bfloat);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        //linearlayoutmanager es el administrador
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

       // adapter=new NotasAdapter(this,notesList);
        adapter=new FirebaseRecyclerAdapter<Notes, NotasAdapter.ViewHolder>(
                Notes.class, //clase
                R.layout.notas,//Interfas grafica
                NotasAdapter.ViewHolder.class,//VIEW HOLDER
                databaseReference.child("Items")// Referencia para busacar los datos Firebase
        ) {
            @Override//AQUI ES DONDE SE LLENA LA VIEW
            protected void populateViewHolder(NotasAdapter.ViewHolder viewHolder, Notes model, int i) {
                viewHolder.count.setText(String.valueOf(model.getCount()));//contador de la bd.
                viewHolder.name.setText(model.getName());
              /*  viewHolder.trash.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        adapter.getRef(position).removeValue();
                    }
                });*/
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0 || dy <0 && fab.isShown()){
                    fab.hide();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,InsertNoteActivity.class);
                startActivity(intent);
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView count,name;
        ImageView trash;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view.findViewById(R.id.cardView);
            count=(TextView) view.findViewById(R.id.count);
            name=(TextView)view.findViewById(R.id.name);
            trash=(ImageView)view.findViewById(R.id.trash);

        }
    }
/*
    //metodo definir la lista
    private void createDatos() {
    notesList=new ArrayList<>();//ARREGLO DE OBJETOS COMO SE DELCARA

        notesList.add(new Notes("001","Calabazas",6));
        notesList.add(new Notes("002","Platano",5));
        notesList.add(new Notes("003","Manzana",1));

        notesList.add(new Notes("004","Melon",7));
        notesList.add(new Notes("005","Sandia",4));
        notesList.add(new Notes("006","Naranja",3));
    }

 */
}


/*
<androidx.appcompat.widget.AppCompatImageView
    android:id="@+id/trash"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@android:drawable/ic_delete">
</androidx.appcompat.widget.AppCompatImageView>
*/
