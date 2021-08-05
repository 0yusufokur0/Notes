package com.resurrection.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditNote extends AppCompatActivity {
    private ImageButton backbtn,deleteBtn;
    private EditText header,content;
    private TextView date;
    private static DatabaseHelper databaseHelper;
    private Boolean noteState = true;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        init();

         id = getIntent().getStringExtra("id");
        getData(id);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteState = true;
                onBackPressed();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteState = false;
                databaseHelper.deleteData(id);
                databaseHelper.firebaseNoteSync(String.valueOf(id),"","");
                onBackPressed();
            }
        });


    }
    void init(){
        backbtn = findViewById(R.id.backBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        header = findViewById(R.id.header);
        content = findViewById(R.id.content);
        date = findViewById(R.id.date);
        databaseHelper = new DatabaseHelper(this);


    }

    private void getData(String id) {
        String stringQueryById = " SELECT * FROM " + ConstantValues.dbTableName + " WHERE " + ConstantValues.dbNoteId + " =\"" + id + "\"";
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(stringQueryById, null);

        while (cursor.moveToNext()){
            String headerTemp = "" + cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteHeader));
            String contentTemp = "" + cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteContent));
            String dateTemp = "" + cursor.getString(cursor.getColumnIndex(ConstantValues.dbNoteDate));
            header.setText(headerTemp);
            content.setText(contentTemp);
            date.setText(dateTemp);
        }

        sqLiteDatabase.close();

    }


    @Override
    public void onBackPressed() {
        if (noteState){
            databaseHelper.updateData(id,header.getText().toString(),content.getText().toString());
            databaseHelper.firebaseNoteSync(String.valueOf(id),header.getText().toString(),content.getText().toString());
        }

        super.onBackPressed();
    }



}