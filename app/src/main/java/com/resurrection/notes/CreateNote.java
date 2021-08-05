package com.resurrection.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateNote extends AppCompatActivity {
    private EditText editHeader, editContent;
    private ImageButton saveNoteBtn;
    private String header, content, date;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        init();

        databaseHelper.requestStoragePermission(CreateNote.this);

        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (databaseHelper.requestStoragePermission(CreateNote.this)){
                    header = editHeader.getText().toString();
                    content = editContent.getText().toString();
                    databaseHelper.addNotes(header, content);
                    onBackPressed();
                }

            }
        });


    }

    private void init() {
        editHeader = findViewById(R.id.editHeader);
        editContent = findViewById(R.id.editContent);
        saveNoteBtn = findViewById(R.id.saveNoteBtn);
        databaseHelper = new DatabaseHelper(CreateNote.this);
    }

    }

