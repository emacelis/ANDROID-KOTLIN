package com.realstatey.emmateamappnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertNoteActivity extends AppCompatActivity {
EditText id,name,count;
Button add;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        id=(EditText)findViewById(R.id.id);
        name=(EditText)findViewById(R.id.name);
        count=(EditText)findViewById(R.id.count);
        add=(Button) findViewById(R.id.bboton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mId=id.getText().toString();
                String nName=name.getText().toString();
                int nCount=Integer.valueOf(count.getText().toString());

                Notes note =new Notes(mId,nName,nCount);
                databaseReference.child("Items").push().setValue(note);
            }
        });

    }
}
