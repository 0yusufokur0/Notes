package com.resurrection.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton createnote;
    private DatabaseHelper databaseHelper;
    private  NoteAdapter noteAdapter;
    private ArrayList<String> noteId,header,content,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // yeni cihazda giriş yapıldığı zaman sqlite id 0 dan başlatarak verdiği için verileri üzerine yazıyor

        init();

        setRecyclerViewData();
        getDataAndSetArrayList();
        firebaseNoteFullSync(this);
        createnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CreateNote.class);
                startActivity(intent);

            }
        });

    }
    private void init(){
        recyclerView = findViewById(R.id.recyclerView);
        createnote = findViewById(R.id.createButton);
        databaseHelper = new DatabaseHelper(this);
        noteId = new ArrayList<>();
        header = new ArrayList<>();
        content = new ArrayList<>();
        date = new ArrayList<>();

    }
    private void setRecyclerViewData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(MainActivity.this,databaseHelper.getDataAndSet());
        recyclerView.setAdapter(noteAdapter);

    }

    public void firebaseNoteFullSync(Context context){
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        databaseReference.child("users").child(firebaseUser.getUid()).child("notes").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                for (int i = 0; i < header.size(); i++) {
                    HashMap<String, String> noteData = new HashMap<>();
                    String id = noteId.get(i);
                    noteData.put("date", databaseHelper.getDate());
                    noteData.put("header", header.get(i));
                    noteData.put("content", content.get(i));


                    databaseReference.child("users").child(firebaseUser.getUid()).child("notes").child(id).setValue(noteData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }

                Toast.makeText(context, "verileri güncellendi", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "notlar güncellenmedi lütfen internet bağlandtısını kontrol ediniz", Toast.LENGTH_SHORT).show();
            }
        });






    }
    public void getDataAndSetArrayList(){

        ArrayList<NoteTemplate> noteTemplateArrayList = new ArrayList<>();

        String selectionQuery = "SELECT * FROM "+ ConstantValues.dbTableName;
        SQLiteDatabase sqLiteDatabase = MainActivity.this.openOrCreateDatabase("Note",MODE_PRIVATE,null);
        Cursor cursor = sqLiteDatabase.rawQuery(selectionQuery,null);

        while (cursor.moveToNext()){
            noteId.add("" +cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteId)));
            header.add("" +cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteHeader)));
            content.add("" +cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteContent)));
            date.add("" +cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteDate)));
        }
        sqLiteDatabase.close();
    }



    @Override
    protected void onResume() {
        setRecyclerViewData();
        super.onResume();

    }
}